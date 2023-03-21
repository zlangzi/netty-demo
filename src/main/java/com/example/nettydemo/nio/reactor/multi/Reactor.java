package com.example.nettydemo.nio.reactor.multi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 *
 * 负责建立监听accept事件
 * @Author: Administrator
 * @Date: 2023/3/18 14:26
 */

public class Reactor implements Runnable{
    private Integer port;

    //Reactor主要责任就是监听SelectorAcceptor
    //Handler
    //Main
    //Reactor
    private Selector selector;

    @Override
    public void run() {

        try {
            while (!Thread.interrupted()){
                //.阻塞
                selector.select();
//                System.out.println("select");
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
//                    System.out.println("分发:"+selectionKey.readyOps());
                    dispatchKey(selectionKey);

                }
            }

        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    private void dispatchKey(SelectionKey selectionKey) throws IOException {
        ((Runnable)selectionKey.attachment()).run();
    }

    public Reactor(Integer port) {
        this.port = port;
        init();
    }

    private void init() {
        try {
            selector = Selector.open();

            //1.得到serverSocketChannel
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            //2.配置不阻塞
            serverSocketChannel.configureBlocking(false);
            //3.绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(this.port));

            //4.注册连接进入事件
            serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT,new Acceptor(selector,serverSocketChannel));

            System.out.println(port + "端口等待连接");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

