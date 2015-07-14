package com.jarvis.zcontrol.spring.userside;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jarvis.zcontrol.bean.JobInfoBean;
import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;
import com.jarvis.zcontrol.spring.serverside.GetJobService;

/**
 * @author zjx 注册crontab
 */
@Component("RegistedService")
public class RegistedServiceImpl implements BaseService {

	private static final Logger LOG = LoggerFactory
			.getLogger(RegistedServiceImpl.class);

	@Autowired
	private GetJobService getJobService;

	@Override
	public void deal(MessageProtocol messageProtocol) {
		LOG.info("新建crontab");
		// TODO

		JobInfoBean jobInfoBean = new JobInfoBean();
		jobInfoBean.setMessageProtocol(messageProtocol);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		
		// TODO
		Calendar clCalendar = Calendar.getInstance();
		clCalendar.setTime(new Date());
		clCalendar.add(Calendar.SECOND, 10);
		jobInfoBean.setExpectRunTime(clCalendar.getTime());
		getJobService.putJobInfoBean(jobInfoBean);

		LOG.info("新建crontab完成");

	}

}
