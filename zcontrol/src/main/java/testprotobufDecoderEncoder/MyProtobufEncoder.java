package testprotobufDecoderEncoder;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;

public class MyProtobufEncoder extends
		MessageToMessageEncoder<MessageLiteOrBuilder> {

	public static byte[] int2byteArray(int num) {
		byte[] result = new byte[4];
		result[0] = (byte) (num >>> 24);// 取最高8位放到0下标
		result[1] = (byte) (num >>> 16);// 取次高8为放到1下标
		result[2] = (byte) (num >>> 8); // 取次低8位放到2下标
		result[3] = (byte) (num); // 取最低8位放到3下标
		return result;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageLiteOrBuilder msg,
			List<Object> out) throws Exception {

		String clazzName = msg.getClass().getName();
		byte[] protobufBs = ((MessageLite) msg).toByteArray();
		byte[] protobufLengthBs = int2byteArray(protobufBs.length);
		byte[] clazzNameBs = int2byteArray(clazzName.getBytes().length);

		// 输出长度 int+clazz类名+int+protobuf内容长度
		byte[] outputBs = new byte[4 + clazzName.getBytes().length + 4
				+ protobufBs.length];
		int index = 0;
		System.arraycopy(clazzNameBs, 0, outputBs, index, 4);
		index += 4;
		System.arraycopy(clazzName.getBytes(), 0, outputBs, index,
				clazzName.getBytes().length);
		index += clazzName.getBytes().length;
		System.arraycopy(protobufLengthBs, 0, outputBs, index, 4);
		index += 4;
		System.arraycopy(protobufBs, 0, outputBs, index, protobufBs.length);

		out.add(wrappedBuffer(outputBs));
		return;
//		if (msg instanceof MessageLite) {
//			out.add(wrappedBuffer(((MessageLite) msg).toByteArray()));
//			return;
//		}
//		if (msg instanceof MessageLite.Builder) {
//			out.add(wrappedBuffer(((MessageLite.Builder) msg).build()
//					.toByteArray()));
//		}

	}

}
