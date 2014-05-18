package ro.pub.cs.pdsd.buddystalker.http.client;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.pdsd.buddystalker.model.User;
import android.os.AsyncTask;

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
