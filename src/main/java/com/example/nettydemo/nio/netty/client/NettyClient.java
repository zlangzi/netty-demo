package com.example.nettydemo.nio.netty.client;

import com.example.nettydemo.nio.netty.encode.MyEncode;
import com.example.nettydemo.nio.netty.encode.MyEncode2;
import com.example.nettydemo.nio.netty.encode.Mydecode;
import com.example.nettydemo.nio.netty.encode.Mydecode2;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


/**
 * @Author: Administrator
 * @Date: 2023/3/18 18:01
 */

public class NettyClient {
    public static void main(String[] args) {

        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ByteBuf buf = Unpooled.copiedBuffer("&".getBytes());
                ch.pipeline()
                        .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                        .addLast(new LengthFieldPrepender(4, 0, false))


                        .addLast(new StringDecoder())
                        .addLast(new StringEncoder())
                        .addLast(new Mydecode2())
                        .addLast(new MyEncode2())
                        .addLast(new Mydecode())
                        .addLast(new MyEncode())
                        .addLast(new ClientHander());
            }
        });
        try {
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8085).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

}

