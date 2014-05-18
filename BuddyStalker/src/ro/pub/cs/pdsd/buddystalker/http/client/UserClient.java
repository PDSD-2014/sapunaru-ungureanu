package ro.pub.cs.pdsd.buddystalker.http.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import ro.pub.cs.pdsd.buddystalker.model.User;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

public class UserClient {
	private static UserClient INSTANCE = null;

	private static final String TAG = UserClient.class.getSimpleName();
	private static final String USER_AGENT = "Android " + Build.VERSION.RELEASE;

	private HttpClient httpClient;

	private static final Gson GSON = new Gson();

	private UserClient() {
		httpClient = AndroidHttpClient.newInstance(USER_AGENT);
	}

	public static UserClient getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new UserClient();
		}

		return INSTANCE;
	}

	public User getUser(long id) throws ClientProtocolException, IOException {
		HttpUriRequest request = new HttpGet(RestApiPaths.USERS_PATH + "/" + id);
		HttpResponse response = httpClient.execute(request);

		String responseBody = EntityUtils.toString(response.getEntity());
		User user = GSON.fromJson(responseBody, User.class);

		return user;
	}

	public User getUserByUsername(String username) throws ClientProtocolException, IOException {
		Uri.Builder uriBuilder = Uri.parse(RestApiPaths.USER_SEARCH_PATH).buildUpon();
		uriBuilder.appendQueryParameter(RestApiParameters.USERNAME_PARAM, username);
		HttpUriRequest request = new HttpGet(uriBuilder.build().toString());
		HttpResponse response = httpClient.execute(request);

		Log.d(TAG, "getUserByUsername status line " + response.getStatusLine());

		String responseBody = EntityUtils.toString(response.getEntity());
		User user = GSON.fromJson(responseBody, User.class);

		return user;
	}

	public List<User> getUsers() throws ClientProtocolException, IOException {
		HttpUriRequest request = new HttpGet(RestApiPaths.USERS_PATH);
		HttpResponse response = httpClient.execute(request);

		Log.d(TAG, "getUsers status line " + response.getStatusLine());

		String responseBody = EntityUtils.toString(response.getEntity());
		User[] users = GSON.fromJson(responseBody, User[].class);

		return Arrays.asList(users);
	}

	public boolean uploadLocation(long userId, double latitude, double longitude)
			throws ClientProtocolException, IOException {
		Uri.Builder uriBuilder = Uri.parse(RestApiPaths.USERS_PATH).buildUpon();
		uriBuilder.appendPath(String.valueOf(userId));
		uriBuilder.appendPath(RestApiPaths.USER_LOCATION_SEGMENT);
		uriBuilder.appendQueryParameter(RestApiParameters.LATITUDE_PARAM, String.valueOf(latitude));
		uriBuilder.appendQueryParameter(RestApiParameters.LONGITUDE_PARAM, String.valueOf(longitude));

		Log.d(TAG, "uploadLocation request URI " + uriBuilder.build().toString());
		HttpPut request = new HttpPut(uriBuilder.build().toString());
		HttpResponse response = httpClient.execute(request);

		Log.d(TAG, "uploadLocation status line " + response.getStatusLine());

		return (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);
	}

	public boolean createUser(User user) throws ClientProtocolException, UnsupportedEncodingException,
			IOException {
		HttpPost request = new HttpPost(RestApiPaths.USERS_PATH);
		request.setEntity(new StringEntity(GSON.toJson(user)));
		request.setHeader("Content-Type", "application/json");

		HttpResponse response = httpClient.execute(request);

		StatusLine statusLine = response.getStatusLine();
		Log.d(TAG, "create user response status line: " + statusLine);

		return (statusLine.getStatusCode() == HttpStatus.SC_CREATED);
	}

	public boolean validateCredentials(String username, String password)
			throws ClientProtocolException, IOException, HttpException {
		Uri.Builder uriBuilder = Uri.parse(RestApiPaths.LOGIN_PATH).buildUpon();
		uriBuilder.appendQueryParameter(RestApiParameters.USERNAME_PARAM, username);
		uriBuilder.appendQueryParameter(RestApiParameters.SECRET_PARAM, password);

		HttpUriRequest request = new HttpPost(uriBuilder.build().toString());

		HttpResponse response = httpClient.execute(request);

		StatusLine statusLine = response.getStatusLine();
		Log.d(TAG, "login response status line: " + statusLine);

		return (statusLine.getStatusCode() == HttpStatus.SC_OK);
	}

	public void close() {
		((AndroidHttpClient) httpClient).close();
	}

	protected void addAuthorizationHeader(HttpUriRequest request, String username, String password) {
		request.addHeader("Authorization", encodeCredentials(username, password));
	}

	private String encodeCredentials(String username, String password) {
		String source = username + ":" + password;
		return "Basic "
				+ Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
	}
}
