package crabgeek.nio.learn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class NIOServer {
    private static ByteBuffer readBuffer;
    private static Selector selector;

    public static void main(String[] args) {
        init();
        listen();
    }

    public static void init() {
        readBuffer = ByteBuffer.allocate(128);
        ServerSocketChannel serverSocketChannel;

        try {
            // 打开服务端SocketChannel
            serverSocketChannel = ServerSocketChannel.open();
            // 设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 设置端口
            serverSocketChannel.socket().bind(new InetSocketAddress(9000), 100);

            // 打开多路复用选择器
            selector = Selector.open();

            // 将选择器注册到ServerSocketChannel上，监听连接请求
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listen() {
        while (true) {
            try {
                // selector 查看是否有注册在Selector上的channel 有网络事件。这个方法是阻塞的。
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handleKey(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void handleKey(SelectionKey key) {
        SocketChannel channel = null;

        try {
            // 如果key的类型是连接请求
            if (key.isAcceptable()) {
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                // 通过 TCP 三次握手，建立和获取获取客户端和服务器的连接SocketChannel
                channel = serverSocketChannel.accept();
                channel.configureBlocking(false);
                // 注册网络读事件
                channel.register(selector, SelectionKey.OP_READ);

                // 如果key的类型是读请求
            } else if (key.isReadable()) {
                channel = (SocketChannel) key.channel();
                // position 变为0，limit = capacity,也就是复位操作，准备把数据写入Buffer了
                readBuffer.clear();
                // 通过 Socket 来读取数据并把数据写入 Buffer 中。
                int count = channel.read(readBuffer);

                if (count > 0) {
                    // 开始从Buffer读数据。
                    readBuffer.flip();

                    String request = StandardCharsets.UTF_8.decode(readBuffer).toString();
                    System.out.println("Server receive the request: " + request);

                    String response = "Server accept the request";
                    channel.write(ByteBuffer.wrap(response.getBytes()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
