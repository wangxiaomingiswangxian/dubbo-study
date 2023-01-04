package com.rpc.servelt;

import com.rpc.servelt.impl.UniversityStudentServiceImpl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SimpleClientImporter {

    public static void main(String[] args) {
        Socket socket = null;
        ObjectOutputStream output = null;
        ObjectInputStream input = null;

        try {
            // 连接服务端
            socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 20889));

            output = new ObjectOutputStream(socket.getOutputStream());

            // 发送调用类名称
            Class serviceClass = UniversityStudentServiceImpl.class;
            output.writeUTF(serviceClass.getName());

            // 发送调用方法名称
            output.writeUTF("study");

            // 发送被调用方法参数类型
            Class<?>[] paramType = new Class[1];
            paramType[0] = String.class;
            output.writeObject(paramType);

            // 发送被调用方法具体参数值
            Object[] arg = new Object[1];
            arg[0] = "math";
            output.writeObject(arg);

            input = new ObjectInputStream(socket.getInputStream());
            System.out.println(input.readObject());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
