package com.jarvis.zcontrol.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;

/**
 * @author zjx
 * 注册crontab
 */
@Component("RegistedService")
public class RegistedServiceImpl implements BaseService {

	private static final Logger LOG = LoggerFactory
			.getLogger(RegistedServiceImpl.class);

	@Override
	public void deal(MessageProtocol messageProtocol) {
		LOG.info("新建crontab");
		// TODO

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOG.info("新建crontab完成");

	}

}
