package ro.pub.cs.pdsd.buddystalker.http.client;

public class RestApiPaths {
	private RestApiPaths() {
		// constants class
	}

	public static final String REST_API_BASE_URL = "http://pdsd.cloudapp.net/rest-api";

	public static final String USERS_PATH = REST_API_BASE_URL + "/users";
	public static final String LOGIN_PATH = USERS_PATH + "/validateCredentials";
	public static final String LOGOUT_PATH = USERS_PATH + "/logout";
	public static final String USER_SEARCH_PATH = USERS_PATH + "/search";

	public static final String USER_LOCATION_SEGMENT = "location";
}
