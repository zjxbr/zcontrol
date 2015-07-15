package com.jarvis.zcontrol.spring.userside;

import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;

/**
 * @author zjx
 *
 */
public interface BaseService<T> {
	
	/**
	 * @function 处理过来的请求
	 * @param messageProtocol
	 */
	public void deal(T t);
	
}
