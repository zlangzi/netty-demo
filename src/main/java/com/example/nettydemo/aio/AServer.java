package com.example.nettydemo.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 20:43
 */

public class AServer {
    static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        //指定pool的形式
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        AsynchronousChannelGroup asyncChannelGroup
                = AsynchronousChannelGroup.withThreadPool(threadPool);
        AsynchronousServerSocketChannel asc = AsynchronousServerSocketChannel.open(asyncChannelGroup);



        asc.bind(new InetSocketAddress(8088));
//        asc.setOption(StandardSocketOptions.TCP_NODELAY, true);

        System.out.println("1--" + Thread.currentThread().getName());
        asc.accept(null, new CompletionHandler<AsynchronousSocketChannel, Object>() {

            @Override
            public void completed(AsynchronousSocketChannel socketChannel, Object attachment) {

                try {
                    System.out.printf("2--%s%s%n", Thread.currentThread().getName(), socketChannel.getRemoteAddress());

                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    // socketChannel异步的读取数据到buffer中
                    socketChannel.read(byteBuffer, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer buffer) {
                            // 打印线程的名字
                            System.out.println("3--" + Thread.currentThread().getName());
                            buffer.flip();
                            System.out.println(new String(buffer.array(), 0, result));
                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer buffer) {
                            exc.printStackTrace();
                        }
                    });
                    System.out.println("来消息了:" + new String(byteBuffer.array(), StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable exc, Object attachment) {
                System.out.println("失败了");

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
            while(true){
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

}

