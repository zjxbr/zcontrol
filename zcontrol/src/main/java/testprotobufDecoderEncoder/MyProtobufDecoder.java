package testprotobufDecoderEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.lang.reflect.Method;
import java.util.List;

import com.google.protobuf.MessageLite;
import com.jarvis.zcontrol.protocol.MessagePB.MessageProtocol;

public class MyProtobufDecoder extends MessageToMessageDecoder<ByteBuf> {

	private static final boolean HAS_PARSER;

	static {
		boolean hasParser = false;
		try {
			// MessageLite.getParsetForType() is not available until protobuf
			// 2.5.0.
			MessageLite.class.getDeclaredMethod("getParserForType");
			hasParser = true;
		} catch (Throwable t) {
			// Ignore
		}

		HAS_PARSER = hasParser;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		// 获取protobuf的包名
		int protobufNameLength = msg.readInt();
		byte[] bsProtobufName = new byte[protobufNameLength];
		msg.readBytes(bsProtobufName);
		String protobufName = new String(bsProtobufName);
		// TODO
		System.out.println("获取protobuf name:" + protobufName);

		// 获取MessageLite
		Class clazz = Class.forName(protobufName);
		Method method = clazz.getMethod("getDefaultInstance", null);
//		MessageProtocol messageProtocol = (MessageProtocol) method
//				.invoke(clazz);
//		MessageLite prototype = messageProtocol.getDefaultInstanceForType();
		
		
		MessageLite prototype = (MessageLite) method
				.invoke(clazz);

		// 获取protobuf内容
		int protobufContentLength = msg.readInt();
		byte[] protobufContentBs = new byte[protobufContentLength];
		msg.readBytes(protobufContentBs);

		// 解析protobuf
		if (HAS_PARSER) {
			out.add(prototype.getParserForType().parseFrom(protobufContentBs));
		} else {
			out.add(prototype.newBuilderForType().mergeFrom(protobufContentBs)
					.build());
		}
	}

}
