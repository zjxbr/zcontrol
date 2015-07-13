package com.jarvis.zcontrol.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zjx 获得spring 配置文件
 */
public final class MySpringContext {

	private static final Logger log = LoggerFactory
			.getLogger(MySpringContext.class);
	private static AbstractApplicationContext instance = null;

	private static final Object[] lock = new Object[0];

	private MySpringContext() {

	}

	public static AbstractApplicationContext getInsance() {
		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					log.info("初始化spring context.");
					instance = new ClassPathXmlApplicationContext(
							new String[] { "application-context.xml" });
					instance.start();
					log.info("初始化spring context完成");
				}
			}
		}

		return instance;
	}
}
