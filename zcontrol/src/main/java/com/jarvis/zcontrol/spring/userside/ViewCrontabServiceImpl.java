package com.jarvis.zcontrol.spring.userside;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.jarvis.zcontrol.protocol.SendCommandPB.SendCommandProtocol;

/**
 * @author zjx 查询crontab
 */
@Component("ViewCrontabService")
public class ViewCrontabServiceImpl implements BaseService<SendCommandProtocol> {

	private static final Logger LOG = LoggerFactory
			.getLogger(ViewCrontabServiceImpl.class);

	@Override
	public void deal(SendCommandProtocol t) {
		System.err.println("hello i am in viewCrontabService");
		// TODO Auto-generated method stub

	}

}
