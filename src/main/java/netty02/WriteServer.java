package netty02;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;

public class WriteServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        Selector selector = Selector.open();
        ssc.register(selector, SelectionKey.OP_ACCEPT, null);

        ssc.bind(new InetSocketAddress(8080));

        while(true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    SocketChannel sc = ssc.accept();
                    sc.configureBlocking(false);
                    SelectionKey scKey = sc.register(selector, 0, null);
                    scKey.interestOps(SelectionKey.OP_READ);
                    // 向客户端发送大量数据
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < 3000000; i++) {
                        stringBuilder.append("a");
                    }
                    ByteBuffer encodeBuffer = Charset.defaultCharset().encode(stringBuilder.toString());
                    // 返回值为实际写入的字节数
                    int write = sc.write(encodeBuffer);
                    System.out.println(write);
                    if (encodeBuffer.hasRemaining()) {
                       // 如果还有剩余内容，关注可写事件 (当写缓冲区为空，可写时触发）
                        scKey.interestOps(scKey.interestOps() + SelectionKey.OP_WRITE);
                        // 把未写完的数据挂载到scKey上
                        scKey.attach(encodeBuffer);
                    }
                } else if (key.isWritable()) {
                    ByteBuffer writeBuffer = (ByteBuffer) key.attachment();
                    SocketChannel channel = (SocketChannel) key.channel();
                    int write = channel.write(writeBuffer);
                    System.out.println(write);
                    // 清理buffer
                    if (!writeBuffer.hasRemaining()) {
                        key.attach(null); // 如果写完，就清除buffer
                        // 不需要关注可写事件
                        key.interestOps(key.interestOps() - SelectionKey.OP_WRITE);
                    }
                }
            }
        }
    }
}
