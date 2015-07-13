package com.jarvis.zcontrol.bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.jarvis.zcontrol.exception.FailedExecuteException;

public class RpcInfoBean {
	private String ip;

	private String computerName;

	private String userName;

	/**
	 * 
	 * @return
	 * @throws FailedExecuteException
	 *             获取不到本机器域名会抛异常
	 */
	public static RpcInfoBean returnRPCInfo() throws FailedExecuteException {
		InetAddress netAddress;
		try {
			netAddress = InetAddress.getLocalHost();

		} catch (UnknownHostException e) {
			throw new FailedExecuteException(e);
		}
		String userName = System.getProperty("user.name");// 获取用户名
		// String computerName = map.get("COMPUTERNAME");// 获取计算机名
		// String userDomain = map.get("USERDOMAIN");// 获取计算机域名

		return new RpcInfoBean(netAddress.getHostAddress(),
				netAddress.getHostName(), userName);
	}

	private RpcInfoBean(String ip, String computerName, String userName) {
		super();
		this.ip = ip;
		this.computerName = computerName;
		this.userName = userName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComputerName() {
		return computerName;
	}

	public void setComputerName(String computerName) {
		this.computerName = computerName;
	}

	@Override
	public String toString() {
		return "RpcInfoBean [ip=" + ip + ", computerName=" + computerName
				+ ", userName=" + userName + "]";
	}

}
