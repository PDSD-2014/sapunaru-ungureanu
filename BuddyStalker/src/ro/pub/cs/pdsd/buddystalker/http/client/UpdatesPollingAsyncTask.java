package ro.pub.cs.pdsd.buddystalker.http.client;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.pdsd.buddystalker.model.User;
import android.os.AsyncTask;

public class UpdatesPollingAsyncTask extends AsyncTask<Void, List<User>, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		List<User> users;
		UserClient client = UserClient.getInstance();

		while (!isCancelled()) {
			try {
				users = client.getUsers();
				publishProgress(users);
			} catch (ClientProtocolException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}

		return null;
	}
}
