package com.jarvis.zcontrol.spring.serverside;

import com.jarvis.zcontrol.bean.JobInfoBean;

public interface GetJobService {

	public JobInfoBean getJobInfoBean();

	public void putJobInfoBean(JobInfoBean jobInfoBean);

}
