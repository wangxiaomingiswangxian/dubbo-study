package com.rpc.servelt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerExport {
    /** 具体暴露的ip port */
    private String ip;
    private int port;

    public ServerExport(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    /** 通过线程池来处理请求 */
    private ExecutorService executor = Executors.newFixedThreadPool(8);

    /**
     * 暴露端口
     * @throws IOException
     */
    public void export() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(ip, port));

        while (true) {
            Socket socket = serverSocket.accept();
            // 接收到连接后，将其封装为Task，交由executor处理
            executor.submit(new Task(socket));
        }
    }

    private static class Task implements Runnable {
        private Socket socket;

        public Task(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            ObjectInputStream input = null;
            ObjectOutputStream output = null;

            try {
                // 获取client发送的数据
                input = new ObjectInputStream(socket.getInputStream());
                // 类名
                String interfaceName = input.readUTF();
                // 方法名
                String methodName = input.readUTF();

                // 具体的参数类型和参数值
                Class<?>[] paramterTypes = (Class<?>[])input.readObject();
                Object[] paramters = (Object[])input.readObject();

                Class<?> interfaceClass = Class.forName(interfaceName);
                Method method = interfaceClass.getMethod(methodName, paramterTypes);

                // 通过反射发起调用
                Object result = method.invoke(interfaceClass.newInstance(), paramters);

                // 将结果值响应给客户端
                output = new ObjectOutputStream(socket.getOutputStream());
                output.writeObject(result);
            } catch (IOException | ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                     InvocationTargetException e) {
                e.printStackTrace();
            } finally {
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (null != input) {
                    try {
                        input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (null != socket) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}