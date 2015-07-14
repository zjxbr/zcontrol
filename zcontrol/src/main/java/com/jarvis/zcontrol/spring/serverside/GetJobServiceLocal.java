package com.jarvis.zcontrol.spring.serverside;

import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.jarvis.zcontrol.bean.JobInfoBean;

@Component("GetJobServiceLocal")
public class GetJobServiceLocal implements GetJobService {

	// 任务队列
	private final TreeSet<JobInfoBean> jobInfoBeans = new TreeSet<JobInfoBean>();

	// poll，put 锁
	private Object[] lock = new Object[0];

	@Override
	public JobInfoBean getJobInfoBean() {
		JobInfoBean rtn;
		synchronized (lock) {
			if (jobInfoBeans.size() == 0) {
				return null;
			}
			// poll 和put 同用一个lock
			try {
				rtn = jobInfoBeans.first();
			} catch (NoSuchElementException e) {
				// donothing
				return null;
			}
			// 如果待处理时间超过1000则什么都不做
			if ((rtn.getExpectRunTime().getTime() - System.currentTimeMillis()) > 1000) {
				return null;
			} else {
				// 否则取出第一个
				rtn = jobInfoBeans.pollFirst();
			}
		}
		return rtn;
	}

	@Override
	public void putJobInfoBean(JobInfoBean jobInfoBean) {
		synchronized (lock) {
			// poll 和put 同用一个lock
			jobInfoBeans.add(jobInfoBean);
		}
	}

}
