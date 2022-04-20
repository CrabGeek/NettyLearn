package netty01;

import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

@Slf4j
public class ChannelDemo1 {
    public static void main(String[] args) {
        // FileChannel
        // 1. 通过输入/输出流间接获取，2。RandomAccessFile

        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            // 准备缓冲区 (10 bytes)
            ByteBuffer byteBuf = ByteBuffer.allocate(10);
            // 从channel 读取数据，向buffer写入
            while (true) {
                int len = channel.read(byteBuf);
                log.debug("读取到的字节数 {}", len);
                // 没有内容了
                if (len < 0) {
                    break;
                }
                // 打印buffer内容
                // flip将bytebuffer切换为读模式
                byteBuf.flip();
                // 检查是否还有剩余的数据
                while (byteBuf.hasRemaining()) {
                    byte b = byteBuf.get();
                    log.debug("实际字节 {}", (char) b);
                }
                // 切换为写模式
                byteBuf.clear();
            }
        } catch (IOException e) {
        }
    }
}
