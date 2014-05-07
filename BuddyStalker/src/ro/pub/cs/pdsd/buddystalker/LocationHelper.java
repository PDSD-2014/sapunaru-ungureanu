package ro.pub.cs.pdsd.buddystalker;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationHelper {
	private LocationManager mLocationManager;

	public LocationHelper(Context context) {
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	public boolean isLocationServiceEnabled() {
		return mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
				mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}

	public Location getLastKnownLocation() {
		Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location == null) {
			// try getting the last known location from the network provider
			location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		}

		return location;
	}
}
