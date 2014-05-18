package ro.pub.cs.pdsd.buddystalker.ui;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.pdsd.buddystalker.http.client.UserClient;
import ro.pub.cs.pdsd.buddystalker.location.LocationHelper;
import android.location.Location;

public class UpdateLocationThread extends Thread {
	private long mUserId;
	private LocationHelper mLocationHelper;

	public UpdateLocationThread(long userId, LocationHelper locationHelper) {
		mLocationHelper = locationHelper;
		mUserId = userId;
	}

	@Override
	public void run() {
		UserClient userClient = UserClient.getInstance();
		while (true) {
			try {
				Location location = mLocationHelper.getLastKnownLocation();
				if (location != null) {
					userClient.uploadLocation(mUserId, location.getLatitude(),
							location.getLongitude());
				}
			} catch (ClientProtocolException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
