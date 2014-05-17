package ro.pub.cs.pdsd.buddystalker.server.service;

import java.util.List;

import ro.pub.cs.pdsd.buddystalker.server.model.User;

public interface UserService {
	User getUser(long id);

	List<User> getUsers();

	void createUser(User user);

	void updateUserLocation(long id, float latitude, float longitude);

	boolean userExists(long id);

	boolean usernameExists(String username);
}
