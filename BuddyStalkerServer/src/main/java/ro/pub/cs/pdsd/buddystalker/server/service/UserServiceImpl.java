package ro.pub.cs.pdsd.buddystalker.server.service;

import java.util.ArrayList;
import java.util.List;

import ro.pub.cs.pdsd.buddystalker.server.model.User;

public class UserServiceImpl implements UserService {

	@Override
	public List<User> getUsers() {
		// TODO: actual implementation
		User dummy = new User();
		dummy.setId(1L);
		dummy.setUsername("dbuddy");
		dummy.setFirstName("Buddy");
		dummy.setLastName("Buddy");
		dummy.setLatitude(44.4325f);
		dummy.setLongitude(26.1039f);
		dummy.setStatus("Buddy Stalker is great!");
		List<User> users = new ArrayList<User>();
		users.add(dummy);

		return users;
	}
}
