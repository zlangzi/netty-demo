package com.example.nettydemo.nio.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.UUID;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 17:56
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String str = (String) msg;

        System.out.println("服务端接收消息：" + str);
        // 写回数据
        ctx.writeAndFlush(UUID.randomUUID()+"&"+"\n");
//        super.channelRead(ctx, msg);
    }

}

