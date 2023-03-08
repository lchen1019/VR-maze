package me.pqpo.smartcropper.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(9999);
            while(true){
                //创建服务器的连接，获取客户端的socket
                Socket socket = serverSocket.accept();
                System.out.println("通啦");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
