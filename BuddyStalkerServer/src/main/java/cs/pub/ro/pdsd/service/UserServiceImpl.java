package cs.pub.ro.pdsd.service;

import java.util.ArrayList;
import java.util.List;

import cs.pub.ro.pdsd.model.User;

public class UserServiceImpl implements UserService {

	@Override
	public List<User> getUsers() {
		// TODO: actual implementation
		User dummy = new User();
		dummy.setId(1L);
		dummy.setName("Buddy");
		dummy.setLatitude(44.4325f);
		dummy.setLongitude(26.1039f);
		List<User> users = new ArrayList<User>();
		users.add(dummy);

		return users;
	}
}
