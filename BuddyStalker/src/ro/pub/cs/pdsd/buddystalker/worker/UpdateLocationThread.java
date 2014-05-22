package ro.pub.cs.pdsd.buddystalker.worker;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.pdsd.buddystalker.http.client.UserClient;
import ro.pub.cs.pdsd.buddystalker.location.LocationHelper;
import android.location.Location;

/**
 * Makes a request to the server every {@value #UPDATE_INTERVAL_MILLIS} milliseconds, updating the
 * current user location data.
 */
public class UpdateLocationThread extends Thread {
	private boolean mAlive = true;
	private long mUserId;
	private LocationHelper mLocationHelper;

	private static final long UPDATE_INTERVAL_MILLIS = 3000L;

	public UpdateLocationThread(long userId, LocationHelper locationHelper) {
		mLocationHelper = locationHelper;
		mUserId = userId;
	}

	@Override
	public void run() {
		UserClient userClient = UserClient.getInstance();
		while (mAlive) {
			try {
				Location location = mLocationHelper.getLastKnownLocation();
				if (location != null) {
					userClient.updateLocation(mUserId, location.getLatitude(),
							location.getLongitude());
				}
			} catch (ClientProtocolException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			// perform the request after some sleeping
			try {
				Thread.sleep(UPDATE_INTERVAL_MILLIS);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public void setAlive(boolean alive) {
		mAlive = alive;
	}
}
