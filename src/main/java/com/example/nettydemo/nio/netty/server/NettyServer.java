package com.example.nettydemo.nio.netty.server;

import com.example.nettydemo.nio.netty.encode.MyEncode;
import com.example.nettydemo.nio.netty.encode.MyEncode2;
import com.example.nettydemo.nio.netty.encode.Mydecode;
import com.example.nettydemo.nio.netty.encode.Mydecode2;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;



/**
 * @Author: Administrator
 * @Date: 2023/3/18 17:43
 */

public class NettyServer {
    public static void main(String[] args) {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap=new ServerBootstrap();
        ServerBootstrap serverBootstrap1 = serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                                .addLast(new LengthFieldPrepender(4, 0, false))

                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new Mydecode2())
                                .addLast(new MyEncode2())
                                .addLast(new Mydecode ())
                                .addLast(new MyEncode())
                                .addLast(new ServerHandler());
                    }
                });

        try {
            ChannelFuture channelFuture = serverBootstrap.bind(8085).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

}

