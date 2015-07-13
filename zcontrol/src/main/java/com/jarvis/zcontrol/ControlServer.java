package com.jarvis.zcontrol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jarvis.zcontrol.protocol.MessagePB;
import com.jarvis.zcontrol.server.handler.ServerRequestHandler;

/**
 * Hello world!
 *
 */
public class ControlServer {
	private static final Logger LOG = LoggerFactory
			.getLogger(ControlServer.class);

	public static void main(String[] args) throws InterruptedException {

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(10);

		try {
			ServerBootstrap b = new ServerBootstrap();
			LOG.info("STARTING SERVER....");
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					// .handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(
									"protobufDecoder",
									new ProtobufDecoder(
											MessagePB.MessageProtocol
													.getDefaultInstance()));
							p.addLast(new ProtobufEncoder());
							p.addLast("MyServerHandler",
									new ServerRequestHandler());// server端处理消息
						}
					}).childOption(ChannelOption.AUTO_READ, true).bind(3128)
					.sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
