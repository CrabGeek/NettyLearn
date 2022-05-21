package cn.itcast.Netty01;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-13 13:57
 */
@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        // 1. 创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup(2); // IO任务，普通任务，定时任务
//        EventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup(); // 没有IO任务
        // 2. 获取下一个事件循环对象
        EventLoop eventLoop = group.next();
        // 3. 执行一个普通任务
        /*eventLoop.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("Ok");
        });*/
        // 4. 执行一个定时任务
        eventLoop.scheduleAtFixedRate(()->{
            log.debug("ok");
        }, 0, 1, TimeUnit.SECONDS);
        log.debug("main");
    }
}
