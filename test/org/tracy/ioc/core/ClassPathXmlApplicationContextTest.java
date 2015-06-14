package org.tracy.ioc.core;

import java.io.IOException;

import org.jdom2.JDOMException;
import org.junit.Test;
import org.tracy.demo.controller.UserController;

public class ClassPathXmlApplicationContextTest {

	@Test
	public void test() throws JDOMException, IOException {
		BeanFactory bf = new ClassPathXmlApplicationContext("bean.xml");
		UserController uc =(UserController) bf.getBean("userController");
		uc.addHandle();
	}

}
