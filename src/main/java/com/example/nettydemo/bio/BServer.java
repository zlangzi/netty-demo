package com.example.nettydemo.bio;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Administrator
 * @Date: 2023/3/18 18:08
 */

public class BServer {

    public static void main(String[] args) {
        try {
            ServerSocket  serverSocket =new ServerSocket(8080);


            while (true){
                System.out.println("开始wait");
                Socket s = serverSocket.accept();
                System.out.println("一条连接");
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(s.getInputStream()));
                System.out.println("="+bufferedReader.readLine());

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

