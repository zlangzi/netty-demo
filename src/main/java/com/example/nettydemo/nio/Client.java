package com.example.nettydemo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Client {

     Selector selector;
     SocketAddress address;

    public static void main(String[] args) {
        new Client(InetSocketAddress.createUnresolved("127.0.0.1",8082)).start();

    }

    public Client(SocketAddress address) {
        this.address = address;
    }
    public void start(){

        try {
            selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(address);

            // 需要把socketChannel注册到多路复用器上
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
            while (true) {
                // 阻塞
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isConnectable()) {
                        System.out.println("Connectable");
                        handlerConnect(key);
                    } else if (key.isReadable()) {
                        System.out.println("Readable");
                        handlerRead(key);
                    } else if (key.isWritable()) {
                        System.out.println("Writable");
                    }else if (key.isAcceptable()){
                        System.out.println("Acceptable");
                    }
                }

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    private  void handlerRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        try {
            socketChannel.read(allocate);
            System.out.println("client msg:" + new String(allocate.array()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private  void handlerConnect(SelectionKey key) throws IOException, InterruptedException {

        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.isConnectionPending()) {
            socketChannel.finishConnect();
        }
        socketChannel.configureBlocking(false);
        socketChannel.write(ByteBuffer.wrap("帅气逼人的client".getBytes()));
        socketChannel.register(selector,SelectionKey.OP_READ);

    }
}
