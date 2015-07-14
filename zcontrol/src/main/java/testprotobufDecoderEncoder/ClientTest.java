package testprotobufDecoderEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

import com.jarvis.zcontrol.bean.RpcInfoBean;
import com.jarvis.zcontrol.exception.FailedExecuteException;
import com.jarvis.zcontrol.protocol.SendCommandPB;
import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;
import com.jarvis.zcontrol.protocol.SendCommandPB.SendCommandProtocol;

/**
 * @author zjx
 * @since 2015-07-13 客户端注册crontab 到服务器</br>
 *
 */
public class ClientTest {
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

					// ch.pipeline().addLast(new MessageEncoder());
					// ch.pipeline().addLast(new ProtobufEncoder());
					ch.pipeline().addLast(new MyProtobufEncoder());

				}
			});

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync();

			// 发送http请求

			RpcInfoBean rpcInfoBean = RpcInfoBean.returnRPCInfo();
			System.out.println(rpcInfoBean);
			MessageProtocol bean = MessageProtocol
					.newBuilder()
					.setFunName("RegistedService")
					// .setMessageBody(
					// "i am messagebodyii am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebody am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebodyi am messagebody")
					.setMessageBody(
							"am messagebodyi am messagebodyadfasdfasdasdfassssssssssssssssssssssssssssssssssssssssfsadf")
					.setIp(rpcInfoBean.getIp())
					.setComputerName(rpcInfoBean.getComputerName())
					.setUserName(rpcInfoBean.getUserName()).build();

			// 发送第一个protobuf
			f.channel().write(bean);
			f.channel().flush();

			f.channel().closeFuture();
			
			Thread.sleep(1000);
			
			f = b.connect(host, port).sync();
			// 发送第二个protobuf
			SendCommandProtocol sendCommandProtocol = SendCommandProtocol
					.newBuilder().setCommand("cmd1").build();

			f.channel().write(sendCommandProtocol);
			f.channel().flush();
		} finally {
			workerGroup.shutdownGracefully();
		}

	}
}
