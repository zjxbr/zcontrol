package com.jarvis.zcontrol.spring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;

/**
 * @author zjx
 * 查询crontab
 */
@Component("ViewCrontabService")
public class ViewCrontabServiceImpl implements BaseService {

	private static final Logger LOG = LoggerFactory
			.getLogger(ViewCrontabServiceImpl.class);

	@Override
	public void deal(MessageProtocol messageProtocol) {
		LOG.info("查询crontab");
		// TODO

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LOG.info("查询crontab完成");

	}

}
