package ro.pub.cs.pdsd.buddystalker.http.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import ro.pub.cs.pdsd.buddystalker.model.User;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

public class UserClient {
	private static final String USER_AGENT = "Android " + Build.VERSION.RELEASE;

	private HttpClient httpClient;

	public UserClient() {
		httpClient = AndroidHttpClient.newInstance(USER_AGENT);
	}

	public List<User> getUsers() throws ClientProtocolException, IOException {
		HttpUriRequest request = new HttpGet(RestApiPaths.USERS_PATH);
		addAuthorizationHeader(request);
		HttpResponse response = httpClient.execute(request);

		String responseBody = EntityUtils.toString(response.getEntity());
		Log.d("UserClient", responseBody);

		Gson gson = new Gson();
		User[] users = gson.fromJson(responseBody, User[].class);

		return Arrays.asList(users);
	}

	public void close() {
		((AndroidHttpClient) httpClient).close();
	}

	protected void addAuthorizationHeader(HttpUriRequest request) {
		request.addHeader("Authorization", encodeCredentials("test", "test"));
	}

	private String encodeCredentials(String username, String password) {
		String source = username + ":" + password;
		return "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
	}
}
