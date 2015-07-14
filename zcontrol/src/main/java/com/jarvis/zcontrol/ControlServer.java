package com.jarvis.zcontrol;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jarvis.zcontrol.bean.JobInfoBean;
import com.jarvis.zcontrol.protocol.MessagePB;
import com.jarvis.zcontrol.protocol.SendCommandPB.SendCommandProtocol;
import com.jarvis.zcontrol.server.handler.ServerRequestHandler;
import com.jarvis.zcontrol.spring.MySpringContext;
import com.jarvis.zcontrol.spring.serverside.GetJobServiceLocal;

public class ControlServer {
	private static final Logger LOG = LoggerFactory
			.getLogger(ControlServer.class);

	public static void main(String[] args) throws InterruptedException {

		// 启动监控线程
		Thread thread = new Thread(new Runnable() {

			private final GetJobServiceLocal getJobService = MySpringContext
					.getInstance().getBean(GetJobServiceLocal.class);

			@Override
			public void run() {
				LOG.info("Scanning-TODO-list-thread启动.");
				JobInfoBean jobInfoBean;
				while (true) {
					try {
						// 获取队列第一个值，按期待执行时间升序排列
						jobInfoBean = getJobService.getJobInfoBean();
						if (jobInfoBean == null) {

							LOG.debug("工作队列是空!");
							Thread.sleep(1000);

							continue;
						} else {
							// sleep 待处理时间
							long sleepTIme = jobInfoBean.getExpectRunTime()
									.getTime() - System.currentTimeMillis();
							if (sleepTIme > 0) {
								Thread.sleep(sleepTIme);
							}

							// 处理任务
							doTask(jobInfoBean);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.setName("Scanning-TODO-list-thread");
		thread.setDaemon(true);
		thread.start();

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

	private static final int port = 31280;

	public static void doTask(JobInfoBean jobInfoBean)
			throws InterruptedException {
		final EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {

					// ch.pipeline().addLast(new MessageEncoder());
					ch.pipeline().addLast(new ProtobufEncoder());

				}
			});

			// Start the client.
			ChannelFuture f = b.connect(
					jobInfoBean.getMessageProtocol().getIp(), port).sync();

			SendCommandProtocol bean = SendCommandProtocol.newBuilder()
					.setCommand("echo haha >> /tmp/aaa").build();

			f.channel().write(bean);
			f.channel().flush();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}
	}
}
