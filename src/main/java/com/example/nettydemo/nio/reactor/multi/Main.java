package com.example.nettydemo.nio.reactor.multi;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 14:26
 */

public class Main {
    public static void main(String[] args) {

        new Reactor(8084).run();
    }

}

