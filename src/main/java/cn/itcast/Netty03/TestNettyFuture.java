package cn.itcast.Netty03;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-17 13:32
 */
@Slf4j
public class TestNettyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        EventLoop eventLoop = group.next();

        Callable<Integer> task = () -> {
            log.debug("执行计算");
            Thread.sleep(1000);
            return 70;
        };

        // Netty Future
        Future<Integer> future = eventLoop.submit(task);

        /*log.debug("等待结果");
        // 同步方式获取结果，阻塞
        Integer integer = future.get();
        log.debug("得到结果 {}", integer);
        group.shutdownGracefully();*/

        // 异步方式获取结果，非阻塞
        future.addListener(tas2 -> {
            Integer integer = future.getNow();
            log.debug("得到结果 {}", integer);
            group.shutdownGracefully();
        });

        log.debug("等待结果");

    }
}
