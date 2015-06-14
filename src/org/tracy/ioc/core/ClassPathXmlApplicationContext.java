package org.tracy.ioc.core;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.tracy.annotation.Component;
import org.tracy.annotation.Inject;
import org.tracy.ioc.exception.NullPackageException;
import org.tracy.ioc.exception.PackageNotFoundException;

public class ClassPathXmlApplicationContext extends ApplicationContext
		implements BeanFactory {
	public ClassPathXmlApplicationContext(String xmlFilePath)
			throws JDOMException, IOException {
		refresh();
		register(analyzeXml(xmlFilePath));
	}

	public List<String> analyzeXml(String xmlFilePath) throws JDOMException,
			IOException {
		SAXBuilder sb = new SAXBuilder();
		Document doc = sb.build(ClassPathXmlApplicationContext.class
				.getClassLoader().getResourceAsStream(xmlFilePath)); // 构造文档对象
		Element root = doc.getRootElement(); // 获取根元素
		List<String> basePath = new ArrayList<String>();
		List<Element> childs = root.getChildren("component-scan");
		for (Element e : childs) {
			String eachPath = e.getChildText("base-package");
			if (!"".equals(eachPath) && eachPath != null) {
				if (!basePath.contains(eachPath)) {
					basePath.add(eachPath);
				}
			}

		}
		// for (String e : basePath) {
		// System.out.println(e);
		// }

		return basePath;
	}

	private <T> void register(List<String> basePath) {
		// 将扫描到的类文件加载到spring的存储容器map中
		if (basePath.size() <= 0) {
			throw new NullPackageException("扫描的包路径为空");
		}
		scan(basePath);

		// 然后将所有依赖的属性一一注入
		loadProperty();
	}

	private <T> void scan(List<String> basePath) {
		ClassLoader cl = getClassLoader();
		for (String packageName : basePath) {
			String packageFileName = packageName.replaceAll("\\.", "/");
			try {
				Enumeration<URL> urls = cl.getResources(packageFileName);
				if (urls.hasMoreElements()) {
					while (urls.hasMoreElements()) {
						URL url = urls.nextElement();
						if ("file".equals(url.getProtocol())) {
							File file = new File(url.getPath());
							loadClass(file, packageName);// 加载bean
						}
					}
				} else {
					throw new PackageNotFoundException("没有找到【" + packageName
							+ "】这个包，请检查是否正确");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 加载类
	 * 
	 * @param file
	 * @param packageName
	 */
	private <T> void loadClass(File file, String packageName) {
		for (File child : file.listFiles()) {

			if (child.isFile()) {
				String childName = child.getName();
				if (childName.endsWith(".class")) {
					try {
						@SuppressWarnings("unchecked")
						Class<? extends T> clazz = (Class<? extends T>) Class
								.forName(packageName
										+ "."
										+ childName.substring(0,
												childName.length() - 6));
						// System.out.println(clazz.getSimpleName());
						loadBean(clazz);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (child.isDirectory()) {
				loadClass(child, packageName + "." + child.getName());
			}
		}

	}

	private <T> void loadBean(Class<? extends T> clazz) {
		Component component = clazz.getAnnotation(Component.class);
		if (component != null) {
			try {
				T t = clazz.newInstance();
				// System.out.println(component.value());
				if (!StringUtils.isBlank(component.value())) {
					map.put(component.value(), t);
				} else {
					map.put(StringUtils.uncapitalize(clazz.getSimpleName()), t);
				}
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

	}

	private <T> void loadProperty() {
		for (Entry<String, Object> entry : map.entrySet()) {
			for (Field field : entry.getValue().getClass().getDeclaredFields()) {
				Inject inject = field.getAnnotation(Inject.class);
				if (inject != null) {
					field.setAccessible(true);
					Object obj = getInjectedBean(inject, field);
					if(obj == null){
						break;
					}
					try {
						field.set(entry.getValue(),obj);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private Object getInjectedBean(Inject inject, Field f) {
		if (!StringUtils.isBlank(inject.name()))
			return getBean(inject.name());

		Object obj = getBean(inject.type());

		if (obj != null)
			return obj;

		return getBean(f.getName());

	}
}
