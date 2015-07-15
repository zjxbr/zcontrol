package com.jarvis.zcontrol.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.MessageLite;

/**
 * @author zjx
 * @function 用来解序列化protobuf,格式如下<br>
 *           字段1:int类型，4bytes，具体protobuf类名<br>
 *           字段2:String类型,任意bytes由字段1指定长度，具体类名<br>
 *           字段3:int类型，protobuf对象序列化后的字节数<br>
 *           字段4:bytes<br>
 */
public class JDProtobufDecoder extends MessageToMessageDecoder<ByteBuf> {

	private static final Logger log = LoggerFactory
			.getLogger(JDProtobufDecoder.class);

	// 为了支持2.5.0以前的protobuf版本，参见以下static代码
	private static final boolean HAS_PARSER;

	static {
		boolean hasParser = false;
		try {
			// MessageLite.getParsetForType() is not available until protobuf
			// 2.5.0.
			MessageLite.class.getDeclaredMethod("getParserForType");
			hasParser = true;
		} catch (Throwable t) {
			// 如果抛异常，则说明没有getParserForType方法
			// 说明是protobuf2.5.0以前版本，HAS_PARSER = false
			// Ignore
		}

		HAS_PARSER = hasParser;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		while (doDecoder(ctx, msg, out)) {
			// decode time + 1
		}
		if (log.isDebugEnabled()) {
			log.debug("Decode 的protobuf个数: " + out.size());
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private boolean doDecoder(ChannelHandlerContext ctx, ByteBuf msg,
			List<Object> out) throws Exception {
		msg.markReaderIndex();
		if (msg.readableBytes() == 0) {
			return false;
		}
		// 获取protobuf的包名
		int protobufNameLength = msg.readInt();
		byte[] bsProtobufName = new byte[protobufNameLength];
		msg.readBytes(bsProtobufName);
		String protobufName = new String(bsProtobufName);

		if (log.isDebugEnabled()) {
			log.debug("获取protobuf name:" + protobufName);
		}

		// 获取MessageLite
		Class clazz = Class.forName(protobufName);
		Method method = clazz.getMethod("getDefaultInstance");

		MessageLite prototype = (MessageLite) method.invoke(clazz);

		// 获取protobuf内容
		int protobufContentLength = msg.readInt();
		// TODO 判断是否一个包装不下，如果不够，则返回，这部分代码没有测试到
		if(msg.readableBytes() < protobufContentLength){
			msg.resetReaderIndex();
			return false;
		}
		byte[] protobufContentBs = new byte[protobufContentLength];
		msg.readBytes(protobufContentBs);

		// 解析protobuf
		if (HAS_PARSER) {
			out.add(prototype.getParserForType().parseFrom(protobufContentBs));
		} else {
			out.add(prototype.newBuilderForType().mergeFrom(protobufContentBs)
					.build());
		}

		return true;
	}

}
