package com.example.nettydemo.nio.netty.encode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 23:10
 */

public class Mydecode extends MessageToMessageDecoder<String> {
    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        System.out.println(msg+"------------------------my decode");
        out.add(msg);

    }
}

