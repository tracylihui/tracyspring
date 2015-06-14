package org.tracy.ioc.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class ApplicationContext implements BeanFactory {
	/**
	 * 用来存储bean对象
	 */
	protected Map<String, Object> map = new HashMap<String, Object>();

	@Override
	public Object getBean(String beanName) {
		return map.get(beanName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(Class<T> requireType) {
		return (T) map
				.get(StringUtils.uncapitalize(requireType.getSimpleName()));
	}

	protected void refresh() {
		map.clear();
	}
	
	protected ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try{
			cl = Thread.currentThread().getContextClassLoader();
		}catch(Exception e){
			cl = ClassPathXmlApplicationContext.class.getClassLoader();
			if(cl == null){
				cl=ClassLoader.getSystemClassLoader();
			}
		}
		
		return cl;
	}
}
