package ro.pub.cs.pdsd.buddystalker.ui;

import ro.pub.cs.pdsd.buddystalker.R;
import ro.pub.cs.pdsd.buddystalker.location.LocationHelper;
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

public class MapActivity extends Activity {
	private static final float DEFAULT_ZOOM_FOR_LOCATION = 13.0f;

	private GoogleMap mGoogleMap;
	private LocationHelper mLocationHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		mLocationHelper = new LocationHelper(this);
		setUpMapIfNeeded();
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
		}

		if (mGoogleMap != null) {
			Location location = null;
			CameraPosition cameraPosition = null;
			
			if (!mLocationHelper.isLocationServiceEnabled()) {
				Toast.makeText(this, "", Toast.LENGTH_LONG).show();
				cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(25, 25))
						.zoom(1)
						.build();
			} else {
				mGoogleMap.setMyLocationEnabled(true);

				location = mLocationHelper.getLastKnownLocation();
				if (location != null) {
					cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(location.getLatitude(), location.getLongitude()))
							.zoom(DEFAULT_ZOOM_FOR_LOCATION)
							.build();
					mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				}
			}
		}
	}
}
