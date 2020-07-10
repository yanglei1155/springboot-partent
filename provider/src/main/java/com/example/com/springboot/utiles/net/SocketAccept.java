package com.example.com.springboot.utiles.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SocketAccept {
   private  static ExecutorService thredPool= Executors.newCachedThreadPool();
    public static void main(String[] args) throws  Exception{
        ServerSocket serverSocket=new ServerSocket(8080);
        System.out.println("服务启动成功");
        while (!serverSocket.isClosed()){
            Socket request =serverSocket.accept();//接收服务器端请求，当没接收到会一直阻塞直到建立联系
            System.out.println("消息来自"+request.toString());
            thredPool.execute(()-> {
                try {
                    InputStream is = request.getInputStream();//获取内容
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    String msg;
                    while ((msg = bufferedReader.readLine()) != null) {
                        if (msg.length() == 0) {
                            break;
                        }
                        System.out.println("客户端消息:" + msg);
                    }
                   OutputStream os=request.getOutputStream();
                   os.write("HTTP/1.1 200 Ok\r\n".getBytes());
                   os.write("Content-Length: 11\r\n\r\n".getBytes());
                   os.write("hello world".getBytes());
                   os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        request.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }
}
