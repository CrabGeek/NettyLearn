package netty02;

import lombok.extern.slf4j.Slf4j;
import util.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MultiThreadServer {
    public static void main(String[] args) throws IOException {
        Thread.currentThread().setName("boss");
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector boss = Selector.open();
        SelectionKey bossKey = ssc.register(boss, 0, null);
        bossKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8080));

        //1. 创建固定数量的worker
        Worker[] works = new Worker[2];
        for (int i = 0; i < works.length; i++) {
            works[i] = new Worker("worker-" + i);
        }
//        Worker worker = new Worker("worker-0");
        AtomicInteger index = new AtomicInteger();
        while(true) {
            boss.select();
            Iterator<SelectionKey> iterator = boss.selectedKeys().iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    log.debug("connected... {}", sc.getRemoteAddress());
                    // 2. 关联selector
                    log.debug("before register...{}", sc.getRemoteAddress());
                    // round robin 轮询
                    works[index.getAndIncrement() % works.length].register(sc);
                    log.debug("after register...{}", sc.getRemoteAddress());
                }
            }
        }
    }

    static class Worker implements Runnable{
        private Thread thread;
        private Selector selector;
        private String name;
        private ConcurrentLinkedQueue<Runnable> queue = new ConcurrentLinkedQueue<>();
        private volatile boolean start = false; // worker还未初始化

        public Worker(String name) {
            this.name = name;
        }

        // 初始化线程和selector
        public void register(SocketChannel sc) throws IOException {
            if (!start) {
                this.thread = new Thread(this, this.name);
                thread.start();
                this.selector = Selector.open();
                this.start = true;
            }
            // 向队列里添加了任务，但任务并没有立即执行
            queue.add(() -> {
                try {
                    sc.register(this.selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, null);
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
            });
            this.selector.wakeup(); // 唤醒selector
        }

        @Override
        public void run() {
            while (true) {
                try {
                    this.selector.select();
                    Runnable task = queue.poll();
                    if (task != null) {
                        task.run();
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(16);
                            SocketChannel sc = (SocketChannel) key.channel();
                            log.debug("Read...{}", sc.getRemoteAddress());
                            sc.read(buffer);
                            buffer.flip();
                            ByteBufferUtil.debugAll(buffer);
                        } else if (key.isWritable()) {

                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
