package com.jarvis.zcontrol.clientside;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;

import com.jarvis.zcontrol.protocol.SendCommandPB;
import com.jarvis.zcontrol.protocol.SendCommandPB.SendCommandProtocol;
import com.jarvis.zcontrol.spring.MySpringContext;

/**
 * @author zjx
 *
 */
public class ClientDaemon {
	public static void main(String[] args) throws InterruptedException {

		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(3);

		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					// .handler(new LoggingHandler(LogLevel.INFO))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch)
								throws Exception {
							ChannelPipeline p = ch.pipeline();
							p.addLast(new LoggingHandler(LogLevel.INFO));
							p.addLast(
									"protobufDecoder",
									new ProtobufDecoder(
											SendCommandPB.SendCommandProtocol
													.getDefaultInstance()));
							p.addLast(new ProtobufEncoder());
							p.addLast("MyServerHandler",
									new ClientCommandHandler());// server端处理消息
						}
					}).childOption(ChannelOption.AUTO_READ, true).bind(31280)
					.sync().channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}

	/**
	 * @author zjx 处理server端发回的命令协议
	 */
	public static class ClientCommandHandler extends
			ChannelInboundHandlerAdapter {

		private static final Logger LOG = LoggerFactory
				.getLogger(ClientCommandHandler.class);

		private static final AbstractApplicationContext SpringContext = MySpringContext
				.getInstance();

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			cause.printStackTrace();
			ctx.close();
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
		public void channelRead(final ChannelHandlerContext ctx,
				final Object msg) throws Exception {
			if (!(msg instanceof SendCommandProtocol)) {
				// TODO
				System.err.println("对象解析错误，无法解析成 ClientCommandHandler");
				LOG.error("对象解析错误，无法解析成 ClientCommandHandler");
				return;
			}
			SendCommandProtocol sendCommandProtocol = (SendCommandProtocol) msg;
			LOG.info("接受请求:" + sendCommandProtocol.toString());
			// TODO
			// 测试做一下
			if (sendCommandProtocol.getCommand() == null) {
				// TODO
				System.err.println("命令行是空");
				return;
			}

			for (String line : runShell(sendCommandProtocol.getCommand())) {
				System.out.println(line);
			}
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx)
				throws Exception {
			ctx.fireChannelReadComplete();
		}

		public static List<String> runShell(String shStr) throws Exception {
			List<String> strList = new ArrayList<String>();

			Process process;
			process = Runtime.getRuntime().exec(
					new String[] { "/bin/sh", "-c", shStr }, null, null);
			InputStreamReader ir = new InputStreamReader(
					process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			String line;
			process.waitFor();
			while ((line = input.readLine()) != null) {
				strList.add(line);
			}

			return strList;
		}
	}
}
