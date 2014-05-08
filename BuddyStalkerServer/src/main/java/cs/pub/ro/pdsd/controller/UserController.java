package cs.pub.ro.pdsd.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cs.pub.ro.pdsd.model.User;

@Controller
public class UserController {
	private final AtomicLong counter = new AtomicLong();

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public List<User> getUsers() {
		List<User> userList = new ArrayList<>();
		userList.add(new User(counter.incrementAndGet(), "TestUser"));

		return userList;
	}
}
