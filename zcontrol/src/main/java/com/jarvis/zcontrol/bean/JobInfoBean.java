package com.jarvis.zcontrol.bean;

import java.util.Date;

import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;

public class JobInfoBean implements Comparable<JobInfoBean> {

	private String name;

	private String command;

	private String crontab; // crontab 的时间格式

	private boolean hasRun; // 已经跑过了

	private Date expectRunTime; // 期待运行时间

	private MessageProtocol messageProtocol;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getCrontab() {
		return crontab;
	}

	public void setCrontab(String crontab) {
		this.crontab = crontab;
	}

	public MessageProtocol getMessageProtocol() {
		return messageProtocol;
	}

	public void setMessageProtocol(MessageProtocol messageProtocol) {
		this.messageProtocol = messageProtocol;
	}

	public boolean isHasRun() {
		return hasRun;
	}

	public void setHasRun(boolean hasRun) {
		this.hasRun = hasRun;
	}

	public Date getExpectRunTime() {
		return expectRunTime;
	}

	public void setExpectRunTime(Date expectRunTime) {
		this.expectRunTime = expectRunTime;
	}

	@Override
	public String toString() {
		return "JobInfoBean [name=" + name + ", command=" + command
				+ ", crontab=" + crontab + ", hasRun=" + hasRun
				+ ", expectRunTime=" + expectRunTime + ", messageProtocol="
				+ messageProtocol + "]";
	}

	@Override
	public int compareTo(JobInfoBean o) {
		int rtn = this.expectRunTime.compareTo(o.expectRunTime);
		// 如果期待运行时间不等，则直接返回，否则比较一下equals，是为了等价性
		return rtn != 0 ? rtn : this.equals(o) ? 0 : 1;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((command == null) ? 0 : command.hashCode());
		result = prime * result + ((crontab == null) ? 0 : crontab.hashCode());
		result = prime * result
				+ ((expectRunTime == null) ? 0 : expectRunTime.hashCode());
		result = prime * result + (hasRun ? 1231 : 1237);
		result = prime * result
				+ ((messageProtocol == null) ? 0 : messageProtocol.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobInfoBean other = (JobInfoBean) obj;
		if (command == null) {
			if (other.command != null)
				return false;
		} else if (!command.equals(other.command))
			return false;
		if (crontab == null) {
			if (other.crontab != null)
				return false;
		} else if (!crontab.equals(other.crontab))
			return false;
		if (expectRunTime == null) {
			if (other.expectRunTime != null)
				return false;
		} else if (!expectRunTime.equals(other.expectRunTime))
			return false;
		if (hasRun != other.hasRun)
			return false;
		if (messageProtocol == null) {
			if (other.messageProtocol != null)
				return false;
		} else if (!messageProtocol.equals(other.messageProtocol))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
