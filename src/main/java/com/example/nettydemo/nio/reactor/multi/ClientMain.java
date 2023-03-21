package com.example.nettydemo.nio.reactor.multi;

import com.example.nettydemo.nio.Client;

import java.net.InetSocketAddress;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 15:48
 */

public class ClientMain {
    public static void main(String[] args) {
        new Client(new InetSocketAddress("127.0.0.1",8084)).start();
    }

}

