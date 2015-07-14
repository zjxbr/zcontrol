package testprotobufDecoderEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;
import com.jarvis.zcontrol.protocol.SendCommandPB.SendCommandProtocol;

/**
 * @author zjx
 * @since 2015-07-13 客户端注册crontab 到服务器</br>
 *
 */
public class ClientTest {
	private static final Logger log = LoggerFactory.getLogger(ClientTest.class);

	public static void main(String[] args) throws InterruptedException,
			FailedExecuteException {

		final String host = "localhost";
		final int port = 3128;

		int i = 0;
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
					// ch.pipeline().addLast(new ProtobufEncoder());
					ch.pipeline().addLast(new MyProtobufEncoder());
					ch.pipeline().addLast(new MyProtobufDecoder());
					ch.pipeline().addLast(new ClientRequestHandler());

				}
			});

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync();
			// f.channel().write

			// Wait until the connection is closed.
			f.channel().closeFuture().sync();
			Thread.sleep(10);
		} finally {
			workerGroup.shutdownGracefully();
		}

	}
}
