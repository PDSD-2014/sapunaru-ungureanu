package ro.pub.cs.pdsd.buddystalker.http.client;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import ro.pub.cs.pdsd.buddystalker.model.User;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

public class UserClient {
	private static final UserClient INSTANCE = null;

	private static final String USER_AGENT = "Android " + Build.VERSION.RELEASE;

	private HttpClient httpClient;

	private UserClient() {
		httpClient = AndroidHttpClient.newInstance(USER_AGENT);
	}

	public static UserClient getInstance() {
		if (INSTANCE == null) {
			return new UserClient();
		} else {
			return INSTANCE;
		}
	}

	public List<User> getUsers() throws ClientProtocolException, IOException {
		HttpUriRequest request = new HttpGet(RestApiPaths.USERS_PATH);
		//addAuthorizationHeader(request);
		HttpResponse response = httpClient.execute(request);

		String responseBody = EntityUtils.toString(response.getEntity());
		Log.d("UserClient", responseBody);

		Gson gson = new Gson();
		User[] users = gson.fromJson(responseBody, User[].class);

		return Arrays.asList(users);
	}

	public void login(String username, String password)
			throws ClientProtocolException, IOException, HttpException {
		HttpUriRequest request = new HttpPost(RestApiPaths.LOGIN_PATH);
		addAuthorizationHeader(request, username, password);

		HttpResponse response = httpClient.execute(request);
		int statusCode = response.getStatusLine().getStatusCode();

		Log.w("UserClient", "login response status is " + statusCode);

		switch (statusCode) {
		case HttpStatus.SC_UNAUTHORIZED:
			throw new HttpException("Username and password combination is invalid");
		}
	}

	public void close() {
		((AndroidHttpClient) httpClient).close();
	}

	protected void addAuthorizationHeader(HttpUriRequest request, String username, String password) {
		request.addHeader("Authorization", encodeCredentials(username, password));
	}

	private String encodeCredentials(String username, String password) {
		String source = username + ":" + password;
		return "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
	}
}
