package com.lagou.handler;

import com.lagou.service.UserServiceImpl;
import com.lagou.vo.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;

/**
 * 自定义的业务处理器
 */
public class UserServiceHandler extends ChannelInboundHandlerAdapter {

    //当客户端读取数据时,该方法会被调用
    public void channelReadOld(ChannelHandlerContext ctx, Object msg) throws Exception {

        //注意:  客户端将来发送请求的时候会传递一个参数:  UserService#sayHello#are you ok
         //1.判断当前的请求是否符合规则
        if(msg.toString().startsWith("UserService")){
            //2.如果符合规则,调用实现类货到一个result
            UserServiceImpl service = new UserServiceImpl();
            String result = service.sayHello(msg.toString().substring(msg.toString().lastIndexOf("#")+1));
            //3.把调用实现类的方法获得的结果写到客户端
            ctx.writeAndFlush(result);
        }


    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // 判断是否符合约定，符合则调用本地方法，返回数据
        // msg:  UserService#sayHello#are you ok?
        if(msg instanceof RpcRequest){
            RpcRequest rpcRequest = (RpcRequest) msg;
            String className = rpcRequest.getClassName();
            String methodName = rpcRequest.getMethodName();
            Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
            Object[] parameters = rpcRequest.getParameters();

            Class<?> clazz = Class.forName(className);
            Method method = clazz.getMethod(methodName, parameterTypes);
            Object bean = GetBeanInstance.getBean(clazz);
            Object result = method.invoke(bean, parameters);
            ctx.writeAndFlush(result);
        }


    }
}
