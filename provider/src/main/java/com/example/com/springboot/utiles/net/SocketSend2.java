package com.example.com.springboot.utiles.net;

import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketSend2 {
    public static void main(String[] args) throws  Exception {
        Socket socket=new Socket("localhost",8080);
        OutputStream os=socket.getOutputStream();
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入");
        String msg=scanner.nextLine();
        os.write(msg.getBytes());
        os.close();
        socket.close();
    }
}
