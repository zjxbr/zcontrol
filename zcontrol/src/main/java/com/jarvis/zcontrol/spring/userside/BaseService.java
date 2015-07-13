package com.jarvis.zcontrol.spring.userside;

import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;

/**
 * @author zjx
 *
 */
public interface BaseService {
	
	/**
	 * @function 处理过来的请求
	 * @param messageProtocol
	 */
	public void deal(MessageProtocol messageProtocol);
	
}
