package com.example.nettydemo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * selector 就是一个门铃的责任，有信息 select就有返回
 *
 *
 * @Author: Administrator
 * @Date: 2023/3/17 18:08
 */

public class Server {
    static Selector selector;
    public static void main(String[] args) {
        try {
            selector=Selector.open();


            ServerSocketChannel serverSocketChannel= ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(8082));

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true){
                //阻塞
                selector.select();

                final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key =iterator.next();
                    iterator.remove();

                    if (key.isConnectable()) {
                        System.out.println("Connectable");
//                        handlerConnect(key);
                    } else if (key.isReadable()) {
                        System.out.println("Readable");
                        handlerRead(key);
                    } else if (key.isWritable()) {
                        System.out.println("Writable");
                    }else if (key.isAcceptable()){
                        System.out.println("Acceptable");
                        handlerAccept(key);
                    }

                }


            }




        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void handlerRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel)key.channel();

        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        int len=socketChannel.read(byteBuffer);
        if (len>0){
            System.out.println("clent:"+socketChannel.getRemoteAddress()+"说"+new String(byteBuffer.array(),StandardCharsets.UTF_8));
        }
        socketChannel.write(ByteBuffer.wrap("帅逼服务器收到.".getBytes(StandardCharsets.UTF_8)));

        //注册读事件
        socketChannel.register(selector,SelectionKey.OP_READ);


    }

    private static void handlerAccept(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel)key.channel();
//        channel.configureBlocking(false);

        //得到socketchannel开始交流
        SocketChannel socketChannel = channel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.write(ByteBuffer.wrap("帅逼服务器连接成功.".getBytes(StandardCharsets.UTF_8)));

        //注册读事件
        socketChannel.register(selector,SelectionKey.OP_READ);



    }

}

