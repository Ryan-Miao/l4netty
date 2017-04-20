package com.test.l4netty.server;

import com.test.l4netty.entity.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by Ryan on 2017/4/20.
 */
public class TimeEncoder extends MessageToByteEncoder<UnixTime>{

    protected void encode(ChannelHandlerContext ctx, UnixTime msg, ByteBuf out) throws Exception {
        out.writeInt((int) msg.value());
    }
}
