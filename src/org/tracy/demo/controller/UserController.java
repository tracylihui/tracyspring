package org.tracy.demo.controller;

import org.tracy.annotation.Component;
import org.tracy.annotation.Inject;
import org.tracy.demo.model.User;
import org.tracy.demo.service.UserService;

@Component("userController")
public class UserController {
	@Inject
	private UserService userService;

	public void addHandle() {
		User user = new User();
		userService.insert(user);
	}
}
