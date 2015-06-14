package org.tracy.ioc.core;

public interface BeanFactory {
	Object getBean(String beanName);

	<T> T getBean(Class<T> requireType);
}
