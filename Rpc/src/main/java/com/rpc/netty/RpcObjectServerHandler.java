package com.rpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

public class RpcObjectServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
        // 当读取到客户端的请求后，则解析对应参数并通过反射发起调用
        if (msg instanceof ServiceInvokeRequest) {
            ServiceInvokeRequest request = (ServiceInvokeRequest) msg;
            String interfaceName = request.getServiceName();
            String methodName = request.getMethodName();

            Class<?>[] paramterTypes = (Class<?>[]) request.getRequestParamType();
            Object[] paramters = (Object[]) request.getArgs();

            Class<?> interfaceClass = Class.forName(interfaceName);
            Method method = interfaceClass.getMethod(methodName, paramterTypes);

            // 最终还是通过反射来调用具体方法
            Object result = method.invoke(interfaceClass.newInstance(), paramters);
            // 调用完成后，将结果值返回给客户端
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
