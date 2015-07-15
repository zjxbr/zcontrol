package com.jarvis.zcontrol.server.handler;

import java.lang.reflect.Method;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;
import com.jarvis.zcontrol.protocol.SpringBeanPB.SpringBeanProtocol;
import com.jarvis.zcontrol.spring.MySpringContext;
import com.jarvis.zcontrol.spring.userside.BaseService;

/**
 * @author zjx
 *
 */
public class ServerRequestHandler extends ChannelInboundHandlerAdapter {

	private static final Logger LOG = LoggerFactory
			.getLogger(ServerRequestHandler.class);

	private static final AbstractApplicationContext SpringContext = MySpringContext
			.getInstance();

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO
	}

	/**
	 * Closes the specified channel after alexl queued write requests are
	 * flushed.
	 */
	static void closeOnFlush(Channel ch) {
		if (ch.isActive()) {
			ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(
					ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg)
			throws Exception {
		System.out.println("getMSG: " + msg);

		// bean.getSpringBeanProtocol();
		Method method = msg.getClass().getMethod("getSpringBeanProtocol");
		SpringBeanProtocol springBeanProtocol = (SpringBeanProtocol) method
				.invoke(msg);
		System.err.println(springBeanProtocol.getBeanName());

		BaseService baseService = SpringContext.getBean(
				springBeanProtocol.getBeanName(), BaseService.class);
		
		baseService.deal(msg);

		// msg.getClass().getMethods();

		// if (!(msg instanceof MessageProtocol)) {
		// // TODO
		// System.err.println("对象解析错误，无法解析成 MessageProtocol");
		// return;
		// }
		// MessageProtocol messageProtocol = (MessageProtocol) msg;
		// LOG.info("接受请求:" + messageProtocol.toString());
		// String functionName = messageProtocol.getFunName();
		//
		// BaseService baseService = SpringContext.getBean(functionName,
		// BaseService.class);
		//
		// baseService.deal(messageProtocol);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channel 读取完毕");
		ctx.fireChannelReadComplete();
	}

}