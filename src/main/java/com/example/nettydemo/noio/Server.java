package com.example.nettydemo.noio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Administrator
 * @Date: 2023/3/17 17:31
 */

public class Server {
    public static List<SocketChannel> channelList = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocketChannel  serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(8080));
            while (true){
                //在监听，这里不会阻塞，会继续续执行下面的语句
                SocketChannel socketChannel=serverSocketChannel.accept();
                //因为不阻塞，这里不判断就会空指针
                if (socketChannel!=null){
                    socketChannel.configureBlocking(false);
                    channelList.add(socketChannel);
                }
                //在处理
                for (SocketChannel client : channelList){
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

                    int num = client.read(byteBuffer);

                    if(num>0){
                        System.out.println("客户端端口："+ client.socket().getPort()+",客户端收据："+new String(byteBuffer.array()));
                        client.write(ByteBuffer.wrap("ok\n".getBytes(StandardCharsets.UTF_8)));
                    }
                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

