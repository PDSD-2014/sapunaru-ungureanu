package ro.pub.cs.pdsd.buddystalker.worker;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.pdsd.buddystalker.http.client.UserClient;
import ro.pub.cs.pdsd.buddystalker.model.User;
import android.os.AsyncTask;

/**
 * Polls the server in order to retrieve user updates.
 */
public class UpdatesPollingAsyncTask extends AsyncTask<Void, List<User>, Void> {
	private static final long POLLING_INTERVAL_MILLIS = 3000L;

	@Override
	protected Void doInBackground(Void... params) {
		List<User> users;
		UserClient client = UserClient.getInstance();

		while (!isCancelled()) {
			try {
				users = client.getUsers();

				// publish the data to the UI thread
				publishProgress(users);
			} catch (ClientProtocolException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			// perform the request after some sleeping
			try {
				Thread.sleep(POLLING_INTERVAL_MILLIS);
			} catch (InterruptedException e) {
				return null;
			}
		}

		return null;
	}
}
