package com.example.nettydemo.nio.reactor.multi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 负责处理accept事件，建立后，创建一个Handler处理读
 * @Author: Administrator
 * @Date: 2023/3/18 14:39
 */

public class Acceptor implements Runnable{

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;

    public Acceptor(Selector selector, ServerSocketChannel serverSocketChannel) {

        System.out.println("Accptor建立");
        this.selector = selector;
        this.serverSocketChannel = serverSocketChannel;
    }

    @Override
    public void run() {

        ServerSocketChannel channel = serverSocketChannel;
//        channel.configureBlocking(false);

        //得到socketchannel开始交流
        SocketChannel socketChannel = null;
        try {
            socketChannel = channel.accept();

            socketChannel.configureBlocking(false);
            socketChannel.write(ByteBuffer.wrap("帅逼服务器连接成功.".getBytes(StandardCharsets.UTF_8)));

            //注册读事件
            socketChannel.register(selector,SelectionKey.OP_READ,new Handler(selector,socketChannel));


        } catch (IOException e) {
            e.printStackTrace();
        }



    }



}

