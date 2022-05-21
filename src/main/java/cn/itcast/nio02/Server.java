package cn.itcast.nio02;

import lombok.extern.slf4j.Slf4j;
import util.ByteBufferUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        // 1. 创建selector，一个selector可以管理多个channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        // 2. 建立selector和channel的联系（注册）
        // SelectionKey 事件发生后，通过它可以知道事件和哪个channel发生的事件
        SelectionKey sscKey = ssc.register(selector, 0, null);
        // key 设置只关注accept事件
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        log.debug("sscKey: {}", sscKey);
        ssc.bind(new InetSocketAddress(8080));

        while(true) {
            // 3.  select 方法，没有事件就阻塞，有事件就会运行
            //  select 方法在事件未处理时不会阻塞，事件发生后要么处理要么取消，不能置之不理
            selector.select();

            // 4. 处理事件,SelectionKeys 内部包含了所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 非常重要，处理一个key，需要将它从selectedKeys中删除，否则下次处理就会有问题
                iterator.remove();
                log.debug("key: {}", key);
                // 5. 区分事件类型
                // 如果是accpet事件
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel sc = channel.accept();
                    sc.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16); // attachment
                    // 将一个byteBuffer作为附件，关联到selectionKey上
                    SelectionKey scKey = sc.register(selector, 0, buffer);
                    // key 设置只关注read事件
                    scKey.interestOps(SelectionKey.OP_READ);
                    log.debug("sc {}", sc);
                } else if (key.isReadable()) { // 如果是read
                   try {
                       SocketChannel channel = (SocketChannel) key.channel();
                       // 获取selectionKey附件
                       ByteBuffer buffer = (ByteBuffer) key.attachment();
                       int read = channel.read(buffer);
                       if (read != -1) {
                           split(buffer);
                           // buffer满了需要扩容
                           if (buffer.position() == buffer.limit()) {
                               ByteBuffer newByteBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                               buffer.flip();
                               newByteBuffer.put(buffer);
                               key.attach(newByteBuffer);
                           }
                       } else {
                           key.cancel();
                       }

                   } catch (IOException e) {
                       e.printStackTrace();
                       // 因为客户端断开了，因此需要将key取消（从 selector的key集合中，真正删除)
                       key.cancel();
                   }
                }
                // 取消事件
//                key.cancel();
            }
        }
    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            // 找到完成消息
            if (source.get(i) == '\n') {
                int length = i + 1 - source.position();
                // 把这条完整消息存入新的ByteBuffer
                ByteBuffer target = ByteBuffer.allocate(length);
                // 从 source读, 向target写
                for (int j = 0; j < length; j++) {
                    target.put(source.get());
                }

                ByteBufferUtil.debugAll(target);
            }
        }
        source.compact();
    }
}
