package com.jarvis.zcontrol.clientside;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.jarvis.zcontrol.bean.RpcInfoBean;
import com.jarvis.zcontrol.exception.FailedExecuteException;
import com.jarvis.zcontrol.protocol.JDProtobufDecoder;
import com.jarvis.zcontrol.protocol.JDProtobufEncoder;
import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;
import com.jarvis.zcontrol.protocol.SendCommandPB.SendCommandProtocol;
import com.jarvis.zcontrol.protocol.SpringBeanPB.SpringBeanProtocol;

/**
 * @author zjx
 * @since 2015-07-13 客户端注册crontab 到服务器</br>
 *
 */
public class ClientRegister {
	public static void main(String[] args) throws InterruptedException,
			FailedExecuteException {
		final EventLoopGroup workerGroup = new NioEventLoopGroup();

		final String host = "localhost";
		final int port = 3128;

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {

					ch.pipeline().addLast(new JDProtobufEncoder());
					ch.pipeline().addLast(new JDProtobufDecoder());

				}
			});

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync();

			// 发送测试message请求
			SpringBeanProtocol springBeanProtocol = SpringBeanProtocol
					.newBuilder().setBeanName("RegistedService").build();
			RpcInfoBean rpcInfoBean = RpcInfoBean.returnRPCInfo();
			System.out.println(rpcInfoBean);
			MessageProtocol bean = MessageProtocol
					.newBuilder()
					.setFunName("aaa")
					// .setMessageBody(
					// "i am messagebodyii am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebody am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebody")
					.setMessageBody(
							"am messagebodyi am messagebodyadfasdfasdasdfassssssssssssssssssssssssssssssssssssssssfsadf")
					.setIp(rpcInfoBean.getIp())
					.setComputerName(rpcInfoBean.getComputerName())
					.setUserName(rpcInfoBean.getUserName())
					.setSpringBeanProtocol(springBeanProtocol).build();
			

			f.channel().write(bean);
			f.channel().flush();

			// 发送一个 注册命令的 消息
			SpringBeanProtocol springBeanProtocol2 = SpringBeanProtocol
					.newBuilder().setBeanName("ViewCrontabService").build();
			SendCommandProtocol sendCommandProtocol = SendCommandProtocol
					.newBuilder().setCommand("echo aa >> /tmp/aa")
					.setSpringBeanProtocol(springBeanProtocol2).build();
			f.channel().write(sendCommandProtocol);
			f.channel().flush();

			f.channel().closeFuture();
		} finally {
			workerGroup.shutdownGracefully();
		}

	}
}
