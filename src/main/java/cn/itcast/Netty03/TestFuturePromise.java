package cn.itcast.Netty03;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-17 14:16
 */
@Slf4j
public class TestFuturePromise {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1. 准备 EventLoop 对象
        NioEventLoopGroup group = new NioEventLoopGroup(2);
        EventLoop eventLoop = group.next();

        // 2. 准备以一个Promise，作为获取结果的容器
        DefaultPromise<Integer> promise = new DefaultPromise(eventLoop);

        new Thread(() -> {
            // 3. 任意一个线程执行计算
            log.debug("开始计算...");
            try {
                int i = 1 / 0;
                Thread.sleep(1000);
                // 4. 计算完毕后向promise 填充结果
                promise.setSuccess(80);
            } catch (InterruptedException e) {
                e.printStackTrace();
                promise.setFailure(e);
            }

        }).start();

        // 5. 接收结果的线程, promise.get()同步阻塞
        log.debug("等待结果...");
        log.debug("结果是...{}", promise.get());
        group.shutdownGracefully();
    }
}
