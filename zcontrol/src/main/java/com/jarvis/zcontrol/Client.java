package com.jarvis.zcontrol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.jarvis.zcontrol.protocol.ZjxProtocol1;

public class Client {
	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
				}
			});

			// Start the client.
			ChannelFuture f = b.connect("localhost", 3128).sync();

			// 发送http请求
			ZjxProtocol1 zjxProtocol1 = new ZjxProtocol1();
			zjxProtocol1.setName("lalalala");

			f.channel().write(zjxProtocol1);
			f.channel().flush();
			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
		}

	}
}
