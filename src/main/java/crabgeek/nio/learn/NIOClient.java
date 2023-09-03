package crabgeek.nio.learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NIOClient {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Worker().start();
        }
    }

    static class Worker extends Thread {
        @Override
        public void run() {
            SocketChannel channel = null;
            Selector selector = null;

            try {
                // 1.创建一个 SocketChannel,用来与服务端连接，并实现网络读写操作。
                channel = SocketChannel.open();
                channel.configureBlocking(false);
                // 指定网络地址，通过三次握手实现 TCP 连接
                channel.connect(new InetSocketAddress("127.0.0.1", 9000));

                // 创建一个 selector 对象， 并把 SocketChannel 注册到 selector 上，并监听 请求连接事件 OP_CONNECT
                selector = Selector.open();
                channel.register(selector, SelectionKey.OP_CONNECT);


                while (true) {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        // 如果key的类型是ConnectAble
                        if (key.isConnectable()) {
                            // 如果完成了3次TCP握手
                            if (channel.finishConnect()) {
                                // 监听网络读事件
                                key.interestOps(SelectionKey.OP_READ);
                                // 向服务器发送消息
                                channel.write(ByteBuffer.wrap(("Hi, I am client " + Thread.currentThread().getName()).getBytes()));
                            } else {
                                key.channel();
                            }
                            // 如果key的类型是可读
                        } else if (key.isReadable()) {
                            ByteBuffer buffer = ByteBuffer.allocate(128);
                            channel.read(buffer);
                            buffer.flip();

                            String response = StandardCharsets.UTF_8.decode(buffer).toString();
                            System.out.println(String.format("[%s] receive response: %s",
                                    Thread.currentThread().getName(), response));
                            Thread.sleep(5000);
                            channel.write(ByteBuffer.wrap("Hello".getBytes()));
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
