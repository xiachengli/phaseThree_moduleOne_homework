package com.lagou;

import com.lagou.client.RPCConsumer;
import com.lagou.service.IUserService;

public class ConsumerBoot {

    //参数定义
    private static final String PROVIDER_NAME = "UserService#sayHello#";

    public static void main(String[] args) throws InterruptedException {

        //1.创建代理对象
        IUserService service = (IUserService) RPCConsumer.createProxy(IUserService.class, PROVIDER_NAME);

        //2.循环给服务器写数据
        while (true){
            String result = service.sayHello("hello~");
            System.out.println(result);
            Thread.sleep(2000);
        }

    }
}
