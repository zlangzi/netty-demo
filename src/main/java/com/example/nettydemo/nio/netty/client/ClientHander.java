package com.example.nettydemo.nio.netty.client;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

// 客户端
public class ClientHander extends ChannelInboundHandlerAdapter {


    /**
     * 客户端连接成功后，就会调用此方法，然后给服务端去发送消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接成功");
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush("客户端消息：" + i);
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String str = (String) msg;
        System.out.println("收到服务端的消息内容"+str);

        super.channelRead(ctx, msg);
    }
}
