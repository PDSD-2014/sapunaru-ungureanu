package ro.pub.cs.pdsd.buddystalker.server.service;

import java.util.List;

import ro.pub.cs.pdsd.buddystalker.server.model.User;

public interface UserService {
	User retrieveUser(long id);

	User retrieveUserByUsername(String username);

	List<User> retrieveUsers();

	void createUser(User user);

	void updateUserLocation(long id, float latitude, float longitude);

	boolean userExists(long id);

	boolean usernameExists(String username);

	boolean validateCredentials(String username, String password);

	void updateStatus(Long id, String status);
}
