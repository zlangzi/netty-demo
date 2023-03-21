package com.example.nettydemo.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 18:08
 */

public class BClient {
    public static void main(String[] args) {
        Socket socket=new Socket();
        try {
            socket.connect(new InetSocketAddress("127.0.0.1",8080));
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("我是好人\n".getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

           socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

