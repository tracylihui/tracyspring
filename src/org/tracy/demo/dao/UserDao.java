package org.tracy.demo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tracy.annotation.Component;
import org.tracy.demo.model.User;

@Component("userDao")
public class UserDao {
	private static final Logger logger = LoggerFactory.getLogger(UserDao.class);

	public void add(User user) {
		logger.info("Ω¯»Î¡ÀUserDao");
	}
}
