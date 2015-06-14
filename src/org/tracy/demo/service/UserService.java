package org.tracy.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tracy.annotation.Component;
import org.tracy.annotation.Inject;
import org.tracy.demo.dao.UserDao;
import org.tracy.demo.model.User;

@Component("userService")
public class UserService {
	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	@Inject
	private UserDao userDao;

	public void insert(User user) {
		logger.info("Ω¯»Î¡ÀUserService");
		userDao.add(user);
	}
}
