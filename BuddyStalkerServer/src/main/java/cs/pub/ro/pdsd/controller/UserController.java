package cs.pub.ro.pdsd.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cs.pub.ro.pdsd.model.User;
import cs.pub.ro.pdsd.service.UserService;
import cs.pub.ro.pdsd.service.UserServiceImpl;

@Controller
@RequestMapping(value = "/rest-api/users")
public class UserController {
	private final UserService userService = new UserServiceImpl();

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<User> getUsers(HttpServletRequest request) {
		return userService.getUsers();
	}
}
