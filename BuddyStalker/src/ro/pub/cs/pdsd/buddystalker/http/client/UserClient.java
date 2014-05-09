package ro.pub.cs.pdsd.buddystalker.http.client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import ro.pub.cs.pdsd.buddystalker.model.User;
import android.net.http.AndroidHttpClient;
import android.os.Build;

public class UserClient {
	private HttpClient httpClient;
	
	public UserClient() {
		httpClient = AndroidHttpClient.newInstance("Android " + Build.VERSION.RELEASE);
	}
	
	public List<User> getUsers() {
		HttpUriRequest request = new HttpGet();
		
		return new ArrayList<User>();
	}
}
