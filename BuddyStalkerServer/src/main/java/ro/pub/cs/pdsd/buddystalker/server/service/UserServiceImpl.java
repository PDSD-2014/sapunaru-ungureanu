package ro.pub.cs.pdsd.buddystalker.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import ro.pub.cs.pdsd.buddystalker.server.model.User;

public class UserServiceImpl implements UserService {
	private Map<Long, User> users = new ConcurrentHashMap<>();
	private AtomicLong sequence = new AtomicLong();

	@Override
	public User getUser(long id) {
		return users.get(id);
	}

	@Override
	public List<User> getUsers() {
		return new ArrayList<User>(users.values());
	}

	@Override
	public void createUser(User user) {
		user.setId(getNextSequenceId());
		users.put(user.getId(), user);
	}

	@Override
	public void updateUserLocation(long id, float latitude, float longitude) {
		User user = users.get(id);
		user.setLatitude(latitude);
		user.setLongitude(longitude);
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
		return sequence.getAndIncrement();
	}
}
