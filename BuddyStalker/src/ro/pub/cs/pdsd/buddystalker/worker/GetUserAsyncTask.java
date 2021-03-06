package ro.pub.cs.pdsd.buddystalker.worker;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.pdsd.buddystalker.http.client.UserClient;
import ro.pub.cs.pdsd.buddystalker.model.User;
import android.os.AsyncTask;

/**
 * Retrieves a user from the server.
 */
public class GetUserAsyncTask extends AsyncTask<String, Void, User> {

	@Override
	protected User doInBackground(String... params) {
		UserClient client = UserClient.getInstance();

		try {
			return client.getUserByUsername(params[0]);
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
