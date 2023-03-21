package com.example.nettydemo.nio.reactor.single;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * 具体的处理读逻辑的Handler
 * @Author: Administrator
 * @Date: 2023/3/18 14:39
 */

public class Handler implements Runnable{

    private SocketChannel socketChannel;

    public Handler(Selector selector, SocketChannel socketChannel) {

        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        try {

            ByteBuffer byteBuffer=ByteBuffer.allocate(1024);

            Thread.sleep(10);
            int len=socketChannel.read(byteBuffer);
            if (len>0){
                System.out.println("clent:"+socketChannel.getRemoteAddress()+"说"+new String(byteBuffer.array(), StandardCharsets.UTF_8));
            }
            socketChannel.write(ByteBuffer.wrap("帅逼服务器收到.\n".getBytes(StandardCharsets.UTF_8)));


        }catch (Exception e){
            e.printStackTrace();
        }



    }
}

