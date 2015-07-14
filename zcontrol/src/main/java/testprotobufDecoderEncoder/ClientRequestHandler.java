package testprotobufDecoderEncoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jarvis.zcontrol.bean.RpcInfoBean;
import com.jarvis.zcontrol.exception.FailedExecuteException;
import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;
import com.jarvis.zcontrol.protocol.SendCommandPB.SendCommandProtocol;

/**
 * @author zjx
 *
 */
public class ClientRequestHandler extends ChannelInboundHandlerAdapter {

	static int i =0;
	private static final Logger LOG = LoggerFactory
			.getLogger(ClientRequestHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx)
			throws FailedExecuteException {

		RpcInfoBean rpcInfoBean = RpcInfoBean.returnRPCInfo();
		System.out.println(rpcInfoBean);
		MessageProtocol bean = MessageProtocol
				.newBuilder()
				.setFunName("RegistedService")
				.setMessageBody(
						"am messagebodyi am messagebodyadfasdfasdasdfassssssssssssssssssssssssssssssssssssssssfsadf")
				.setIp(rpcInfoBean.getIp())
				.setComputerName(rpcInfoBean.getComputerName())
				.setUserName(rpcInfoBean.getUserName()).build();

		// 发送第一个protobuf
		ctx.write(bean);

		// 发送第二个protobuf
		SendCommandProtocol sendCommandProtocol = SendCommandProtocol
				.newBuilder().setCommand("cmd1").build();

		ctx.write(sendCommandProtocol);

		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		System.out.println(i++);
		ctx.fireChannelRead(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		System.out.println("client channel read complete");
		RpcInfoBean rpcInfoBean = RpcInfoBean.returnRPCInfo();
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
		ctx.channel().writeAndFlush(bean);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.err.println("client side inactive.");
		ctx.fireChannelInactive();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

}