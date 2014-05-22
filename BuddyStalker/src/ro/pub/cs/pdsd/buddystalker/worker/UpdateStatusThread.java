package ro.pub.cs.pdsd.buddystalker.worker;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.pdsd.buddystalker.http.client.UserClient;

public class UpdateStatusThread extends Thread {
	private String mStatus;
	private long mUserId;

	public UpdateStatusThread(long userId, String status) {
		mUserId = userId;
		mStatus = status;
	}

	@Override
	public void run() {
		UserClient client = UserClient.getInstance();

		try {
			client.updateStatus(mUserId, mStatus);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
