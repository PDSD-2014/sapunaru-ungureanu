package ro.pub.cs.pdsd.buddystalker.http.client;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import ro.pub.cs.pdsd.buddystalker.model.User;
import android.os.AsyncTask;

public class GetUsersAsyncTask extends AsyncTask<Integer, Integer, List<User>> {

	@Override
	protected List<User> doInBackground(Integer... params) {
		List<User> users;
		UserClient client = UserClient.getInstance();

		try {
			users = client.getUsers();
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			client.close();
		}

		return users;
	}
}
