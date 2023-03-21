package com.example.nettydemo.nio.reactor.single;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 14:26
 */

public class Main {
    public static void main(String[] args) {

        new Reactor(8083).run();
    }

}

