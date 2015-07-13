package com.jarvis.zcontrol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import com.jarvis.zcontrol.server.handler.ServerRequestHandler;


/**
 * @author zjx
 *
 */
public class ClientDaemon {
	public static void main(String[] args) throws InterruptedException {

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(10);

		// ServerBootstrap boot = new ServerBootstrap();
		// boot.group(bossGroup, workerGroup)
		// .channel(NioServerSocketChannel.class)
		// .childHandler(new ChannelInitializer<SocketChannel>() {
		// @Override
		// protected void initChannel(SocketChannel ch)
		// throws Exception {
		// ChannelPipeline p = ch.pipeline();
		// p.addLast(new LoggingHandler(LogLevel.INFO));
		// // p.addLast("encoder", new MessageEncoder());// 在这里
		// p.addLast("decoder", new MessageDecoder());// 在这里
		// p.addLast("MyServerHandler", new TestHandler());// server端处理消息
		// }
		// });

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					// 不显示log，打印log需要在这里打开注释
					// .handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(new LoggingHandler(LogLevel.INFO));
							// p.addLast("encoder", new MessageEncoder());// 在这里
//							p.addLast("decoder", new MessageDecoder());// 在这里
							p.addLast("MyServerHandler", new ServerRequestHandler());// server端处理消息
						}
					}).childOption(ChannelOption.AUTO_READ, true).bind(3128)
					.sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
}
