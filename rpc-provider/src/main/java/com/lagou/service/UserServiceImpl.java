package com.lagou.service;

import com.lagou.com.lagou.encode.RpcDecoder;
import com.lagou.com.lagou.serializer.imp.JSONSerializer;
import com.lagou.handler.UserServiceHandler;
import com.lagou.vo.RpcRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    //将来客户端要远程调用的方法
    public String sayHello(String msg) {
        System.out.println("are you ok ? "+msg);
        return "success";
    }


    //创建一个方法启动服务器
    public static void startServer(String ip , int port) throws InterruptedException {
        //1.创建两个线程池对象
        NioEventLoopGroup  bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup  workGroup = new NioEventLoopGroup();

        //2.创建服务端的启动引导对象
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        //3.配置启动引导对象
        serverBootstrap.group(bossGroup,workGroup)
                //设置通道为NIO
                .channel(NioServerSocketChannel.class)
                //创建监听channel
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        //获取管道对象
                        ChannelPipeline pipeline = nioSocketChannel.pipeline();
                        //给管道对象pipeLine 设置编码
                        pipeline.addLast(new StringEncoder());
                        //String序列化
                       //pipeline.addLast(new StringDecoder());
                        //JSON序列化
                        pipeline.addLast(new RpcDecoder(RpcRequest.class,new JSONSerializer()));
                        //把我们自顶一个ChannelHander添加到通道中
                        pipeline.addLast(new UserServiceHandler());
                    }
                });

        //4.绑定端口
        serverBootstrap.bind(ip,port).sync();
    }
}
