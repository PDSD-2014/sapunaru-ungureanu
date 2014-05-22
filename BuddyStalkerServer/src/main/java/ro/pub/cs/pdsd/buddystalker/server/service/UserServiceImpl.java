package ro.pub.cs.pdsd.buddystalker.server.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import ro.pub.cs.pdsd.buddystalker.server.model.User;

public class UserServiceImpl implements UserService {
	private static UserServiceImpl INSTANCE;

	// in-memory database, the key is the user's id
	private Map<Long, User> users = new ConcurrentHashMap<>();
	private AtomicLong sequence = new AtomicLong();

	private static final DateFormat DF = new SimpleDateFormat("E HH:mm, MMM dd");
	static {
		DF.setTimeZone(TimeZone.getTimeZone("GMT+3"));
	}

	private UserServiceImpl() {
		// singleton
	}

	public static UserServiceImpl getInstance() {
		if (INSTANCE == null) {
			return new UserServiceImpl();
		}

		return INSTANCE;
	}

	@Override
	public User retrieveUser(long id) {
		return users.get(id);
	}

	@Override
	public User retrieveUserByUsername(String username) {
		for (User user : users.values()) {
			if (user.getUsername().equals(username)) {
				return user;
			}
		}

		return null;
	}

	@Override
	public List<User> retrieveUsers() {
		return new ArrayList<User>(users.values());
	}

	@Override
	public void createUser(User user) {
		user.setId(getNextSequenceId());
		users.put(user.getId(), user);
	}

	@Override
	public void updateStatus(Long id, String status) {
		User user = users.get(id);
		user.setStatus(status);
	}

	@Override
	public void updateUserLocation(long id, float latitude, float longitude) {
		User user = users.get(id);
		user.setLatitude(latitude);
		user.setLongitude(longitude);
		// the user updated his location, update his "lastSeenAt" date
		user.setLastSeenAt(DF.format(new Date()));
	}

	@Override
	public boolean validateCredentials(String username, String password) {
		for (User user : users.values()) {
			if (user.getUsername().equals(username)) {
				if (user.getPassword().equals(password)) {
					// the user signed in, update his "lastSeenAt" date
					user.setLastSeenAt(DF.format(new Date()));
					return true;
				} else {
					return false;
				}
			}
		}

		return false;
	}

	@Override
	public boolean userExists(long id) {
		return users.containsKey(id);
	}

	@Override
	public boolean usernameExists(String username) {
		for (User user : users.values()) {
			if (user.getUsername().equals(username)) {
				return true;
			}
		}

		return false;
	}

	private long getNextSequenceId() {
		return sequence.incrementAndGet();
	}
}
