package com.example.nettydemo.nio.reactor.multi;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 具体的处理读逻辑的Handler
 * @Author: Administrator
 * @Date: 2023/3/18 14:39
 */

public class Handler implements Runnable{

    static ExecutorService pool= Executors.newCachedThreadPool();

    private SocketChannel socketChannel;
    private Selector selector;

    public Handler(Selector selector, SocketChannel socketChannel) {
        this.selector = selector;

        System.out.println("handler建立");
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        System.out.println("handle");
//        AtomicInteger i= new AtomicInteger();
        //Todo 线程池执行会触发无数次？why
        pool.execute(()->{
//            System.out.println("执行："+i.getAndIncrement());
            try {


                ByteBuffer byteBuffer=ByteBuffer.allocate(1024);

                StringBuffer sb= new StringBuffer();
                int len=socketChannel.read(byteBuffer);
                if (len>0){
                    while (len > 0) {
                        byteBuffer.flip();
                        sb.append(new String(byteBuffer.array(), 0, len));
                        byteBuffer.clear();
                        len = socketChannel.read(byteBuffer);

                    }
                    System.out.println(sb);


                    socketChannel.write(ByteBuffer.wrap("帅逼服务器收到.".getBytes(StandardCharsets.UTF_8)));
                }


            }catch (Exception e){
                e.printStackTrace();
            }
        });

//        pool.execute(new ReaderHandler(this.socketChannel));

    }

}


