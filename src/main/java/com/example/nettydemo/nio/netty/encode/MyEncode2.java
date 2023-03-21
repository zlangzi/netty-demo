package com.example.nettydemo.nio.netty.encode;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 23:10
 */

public class MyEncode2 extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        System.out.println("============my encode2===========================");
        out.add(msg);

    }
}

