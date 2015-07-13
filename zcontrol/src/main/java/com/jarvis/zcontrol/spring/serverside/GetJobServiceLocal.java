package com.jarvis.zcontrol.spring.serverside;

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

		synchronized (lock) {
			// poll 和put 同用一个lock
			return jobInfoBeans.pollFirst();
		}

	}

	@Override
	public void putJobInfoBean(JobInfoBean jobInfoBean) {
		synchronized (lock) {
			// poll 和put 同用一个lock
			jobInfoBeans.add(jobInfoBean);
		}
	}

}
