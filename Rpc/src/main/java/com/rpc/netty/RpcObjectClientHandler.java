package com.rpc.netty;

import com.rpc.servelt.impl.UniversityStudentServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RpcObjectClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 连接建立成功后,即发送请求
        // 客户端拼接需要调用的类、方法、参数的具体信息到ServiceInvokeRequest
        ServiceInvokeRequest request = new ServiceInvokeRequest();
        Class c = UniversityStudentServiceImpl.class;
        request.setServiceName(c.getName());

        request.setMethodName("study");

        Class<?>[] paramType = new Class[1];
        paramType[0] = String.class;
        request.setRequestParamType(paramType);

        Object[] args = new Object[1];
        args[0] = "math";
        request.setArgs(args);

        // 最终将request发送到服务端
        ctx.writeAndFlush(request);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 在这里接收到服务端的响应结果
        System.out.println(msg);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
