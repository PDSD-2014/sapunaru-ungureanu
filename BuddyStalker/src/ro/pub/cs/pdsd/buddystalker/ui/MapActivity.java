package ro.pub.cs.pdsd.buddystalker.ui;

import java.util.List;

import ro.pub.cs.pdsd.buddystalker.R;
import ro.pub.cs.pdsd.buddystalker.http.client.GetUsersAsyncTask;
import ro.pub.cs.pdsd.buddystalker.location.LocationHelper;
import ro.pub.cs.pdsd.buddystalker.model.User;
import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
	private static final float DEFAULT_ZOOM_FOR_LOCATION = 13.0f;
	private static final float DEFAULT_ZOOM_NO_LOCATION = 1.0f;

	private static final LatLng DEFAULT_LAT_LNG = new LatLng(0.0f, 0.0f);

	private GoogleMap mGoogleMap;
	private LocationHelper mLocationHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mLocationHelper = new LocationHelper(this);
		setUpMapIfNeeded();

		Toast.makeText(this, "Logged in as", Toast.LENGTH_SHORT).show();

		new GetUsersAsyncTask() {
			@Override
			protected void onPostExecute(List<User> users) {
				if (users != null) {
					for (User user : users) {
						addUserMarker(user);
					}
				}
			}
		}.execute();
	}

	@Override
	public void onResume() {
		super.onResume();

		setUpMapIfNeeded();
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

			//mGoogleMap.setInfoWindowAdapter(adapter);
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

	private Marker addUserMarker(User user) {
		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(new LatLng(user.getLatitude(), user.getLongitude()));
		markerOptions.title(user.getFirstName() + " " + user.getLastName());
		markerOptions.snippet("Status: " + user.getStatus());

		return mGoogleMap.addMarker(markerOptions);
	}
}
