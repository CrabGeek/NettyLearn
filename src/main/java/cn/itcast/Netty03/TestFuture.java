package cn.itcast.Netty03;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-17 13:23
 */
@Slf4j
public class TestFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Future<Integer> future = executorService.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.debug("执行计算");
                Thread.sleep(1000);
                return 50;
            }
        });
        // 3. 主线程get阻塞等待结果
        log.debug("等待结果");
        Integer integer = future.get();
        System.out.println("获取结果 " + integer);
        executorService.shutdown();
    }
}
