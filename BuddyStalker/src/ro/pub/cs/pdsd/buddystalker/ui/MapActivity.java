package ro.pub.cs.pdsd.buddystalker.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.pub.cs.pdsd.buddystalker.R;
import ro.pub.cs.pdsd.buddystalker.location.LocationHelper;
import ro.pub.cs.pdsd.buddystalker.model.User;
import ro.pub.cs.pdsd.buddystalker.worker.GetUserAsyncTask;
import ro.pub.cs.pdsd.buddystalker.worker.UpdateLocationThread;
import ro.pub.cs.pdsd.buddystalker.worker.UpdatesPollingAsyncTask;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
	private static final float DEFAULT_ZOOM_FOR_LOCATION = 13.0f;
	private static final float DEFAULT_ZOOM_NO_LOCATION = 1.0f;

	private static final LatLng DEFAULT_LAT_LNG = new LatLng(0.0f, 0.0f);

	private Map<String, Marker> markerCache = new HashMap<String, Marker>();

	private GoogleMap mGoogleMap;
	private LocationHelper mLocationHelper;
	private UpdatesPollingAsyncTask mUpdatesPollingTask;
	private UpdateLocationThread mUpdateThread;

	private User currentUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mLocationHelper = new LocationHelper(this);
		setUpMapIfNeeded();

		String username = getIntent().getExtras().getString(ExtraParameters.USERNAME);

		new GetUserAsyncTask() {
			@Override
			protected void onPostExecute(User user) {
				currentUser = user;
				Toast.makeText(MapActivity.this, "Logged in as " + currentUser.getUsername(),
						Toast.LENGTH_SHORT).show();

				if (mUpdateThread == null) {
					mUpdateThread = new UpdateLocationThread(currentUser.getId(), mLocationHelper);
					mUpdateThread.start();
				}
			}
		}.execute(username);

		mUpdatesPollingTask = new UpdatesPollingAsyncTask() {
			@Override
			public void onProgressUpdate(List<User>... users) {
				addUserMarkers(users[0]);
			}
		};
		mUpdatesPollingTask.execute();
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mUpdatesPollingTask != null) {
			mUpdatesPollingTask.cancel(true);
			mUpdatesPollingTask = null;
		}
		if (mUpdateThread != null) {
			try {
				mUpdateThread.setAlive(false);
				mUpdateThread.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			mUpdateThread.interrupt();
			mUpdateThread = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		setUpMapIfNeeded();

		if (mUpdatesPollingTask == null) {
			mUpdatesPollingTask = new UpdatesPollingAsyncTask() {
				@Override
				public void onProgressUpdate(List<User>... users) {
					addUserMarkers(users[0]);
				}
			};
			mUpdatesPollingTask.execute();
		}

		if (mUpdateThread == null && currentUser != null) {
			mUpdateThread = new UpdateLocationThread(currentUser.getId(), mLocationHelper);
			mUpdateThread.start();
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		if (mUpdatesPollingTask != null) {
			mUpdatesPollingTask.cancel(true);
			mUpdatesPollingTask = null;
		}
		if (mUpdateThread != null) {
			try {
				mUpdateThread.setAlive(false);
				mUpdateThread.join();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			mUpdateThread.interrupt();
			mUpdateThread = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setUpMapIfNeeded() {
		// do a null check to confirm that we have not already instantiated the map
		if (mGoogleMap == null) {
			mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

			mGoogleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this));
		}

		if (mGoogleMap != null) {
			Location location = null;
			CameraPosition cameraPosition = null;

			if (!mLocationHelper.isLocationServiceEnabled()) {
				Toast.makeText(this, getString(R.string.toast_location_disabled),
						Toast.LENGTH_LONG).show();
				cameraPosition = new CameraPosition.Builder()
						.target(DEFAULT_LAT_LNG)
						.zoom(DEFAULT_ZOOM_NO_LOCATION)
						.build();
			} else {
				mGoogleMap.setMyLocationEnabled(true);

				location = mLocationHelper.getLastKnownLocation();
				if (location != null) {
					cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(location.getLatitude(), location.getLongitude()))
							.zoom(DEFAULT_ZOOM_FOR_LOCATION)
							.build();
				} else {
					cameraPosition = new CameraPosition.Builder()
							.target(DEFAULT_LAT_LNG)
							.zoom(DEFAULT_ZOOM_NO_LOCATION)
							.build();
				}
			}

			mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		}
	}

	private void addUserMarkers(List<User> users) {
		if (users != null) {
			Marker marker;

			for (User user : users) {
				if (user.getUsername().equals(currentUser.getUsername())) {
					continue;
				}

				if (!markerCache.containsKey(user.getUsername())) {
					MarkerOptions markerOptions = new MarkerOptions();
					markerOptions.position(new LatLng(user.getLatitude(), user.getLongitude()));
					markerOptions.title(user.getFirstName() + " " + user.getLastName());
					markerOptions.snippet(user.getStatus() + CustomInfoWindowAdapter.SNIPPET_DELIMITER
							+ user.getLastSeenAt());
					markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
							BitmapDescriptorFactory.HUE_BLUE));

					marker = mGoogleMap.addMarker(markerOptions);
					markerCache.put(user.getUsername(), marker);
				} else {
					marker = markerCache.get(user.getUsername());
					marker.setPosition(new LatLng(user.getLatitude(), user.getLongitude()));
					marker.setTitle(user.getFirstName() + " " + user.getLastName());
					marker.setSnippet(user.getStatus() + CustomInfoWindowAdapter.SNIPPET_DELIMITER
							+ user.getLastSeenAt());
				}
			}
		}
	}
}
