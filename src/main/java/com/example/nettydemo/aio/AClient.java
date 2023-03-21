package com.example.nettydemo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 20:49
 */

public class AClient {
    static CountDownLatch latch=new CountDownLatch(1);
    public static void main(String[] args) throws IOException, InterruptedException {
        AsynchronousSocketChannel socketChannel= AsynchronousSocketChannel.open();

        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8088), new Writer(socketChannel), new CompletionHandler<Void, Object>() {
            @Override
            public void completed(Void result, Object attachment) {
                System.out.println("连接成功");
                ((Runnable)attachment).run();
                new Reader(socketChannel).run();
            }

            @Override
            public void failed(Throwable exc, Object attachment) {

                System.out.println("连接失败");
            }
        });
        latch.await();




    }

    static class Writer implements Runnable{

        private AsynchronousSocketChannel socketChannel;

        public Writer(AsynchronousSocketChannel socketChannel) {

            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            socketChannel.write(ByteBuffer.wrap("异步也是好人".getBytes(StandardCharsets.UTF_8)), null, new CompletionHandler<Integer, Object>() {
                @Override
                public void completed(Integer result, Object attachment) {
                    System.out.println("写:"+result+attachment);
                }

                @Override
                public void failed(Throwable exc, Object attachment) {
                    System.out.println(exc);

                }
            });
        }
    }
    static class Reader implements Runnable{

        private AsynchronousSocketChannel socketChannel;

        public Reader(AsynchronousSocketChannel socketChannel) {

            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer buffer) {
                    // 打印线程的名字
                    System.out.println("3--" + Thread.currentThread().getName());
                    buffer.flip();
                    System.out.println(new String(buffer.array(), 0, result));
                    socketChannel.write(ByteBuffer.wrap("HelloServert".getBytes()));
                }

                @Override
                public void failed(Throwable exc, ByteBuffer buffer) {
                    exc.printStackTrace();
                }
            });
        }
    }



}

