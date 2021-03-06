package ro.pub.cs.pdsd.buddystalker.server.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ro.pub.cs.pdsd.buddystalker.server.exception.BadRequestException;
import ro.pub.cs.pdsd.buddystalker.server.exception.InvalidCredentialsException;
import ro.pub.cs.pdsd.buddystalker.server.exception.UserNotFoundException;
import ro.pub.cs.pdsd.buddystalker.server.exception.UsernameValidationException;
import ro.pub.cs.pdsd.buddystalker.server.model.User;
import ro.pub.cs.pdsd.buddystalker.server.service.UserService;
import ro.pub.cs.pdsd.buddystalker.server.service.UserServiceImpl;

@Controller
@RequestMapping(value = "/rest-api/users")
public class UserController {
	private final UserService userService = UserServiceImpl.getInstance();

	@RequestMapping(value = "/validateCredentials", method = RequestMethod.POST)
	public ResponseEntity<String> validateCredentials(@RequestParam("username") String username,
			@RequestParam("secret") String password) {
		if (!userService.validateCredentials(username, password)) {
			throw new InvalidCredentialsException();
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public User getUser(@PathVariable Long id) {
		User user = userService.retrieveUser(id);

		if (user == null) {
			throw new UserNotFoundException();
		}

		return user;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public User getUserByUsername(@RequestParam("username") String username) {
		User user = userService.retrieveUserByUsername(username);

		if (user == null) {
			throw new UserNotFoundException();
		}

		return user;
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<User> getUsers() {
		return userService.retrieveUsers();
	}

	@RequestMapping(value = "/{id}/status", method = RequestMethod.PUT)
	public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestBody String status) {
		if (!userService.userExists(id)) {
			throw new UserNotFoundException();
		}

		if (status == null) {
			throw new BadRequestException();
		}

		userService.updateStatus(id, status);

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> createUser(@RequestBody User user) {
		if (!userService.usernameExists(user.getUsername())) {
			userService.createUser(user);
			return new ResponseEntity<String>(HttpStatus.CREATED);
		} else {
			throw new UsernameValidationException();
		}
	}

	@RequestMapping(value = "/{id}/location", method = RequestMethod.PUT)
	public ResponseEntity<String> updateUserLocation(@PathVariable Long id,
			@RequestParam("latitude") Float latitude,
			@RequestParam("longitude") Float longitude) {

		if (!userService.userExists(id)) {
			throw new UserNotFoundException();
		}

		if (latitude == null || longitude == null) {
			throw new BadRequestException();
		}

		userService.updateUserLocation(id, latitude, longitude);

		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
