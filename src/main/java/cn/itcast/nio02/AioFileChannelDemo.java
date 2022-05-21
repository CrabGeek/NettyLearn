package cn.itcast.nio02;

import lombok.extern.slf4j.Slf4j;
import util.ByteBufferUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-10 16:28
 */
@Slf4j
public class AioFileChannelDemo {
    public static void main(String[] args) {
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(Paths.get("data.txt"), StandardOpenOption.READ)) {
            // 参数1： ByteBuffer
            // 参数2：读取的起始位置
            // 参数3：附件
            // 参数4： 回调对象
            ByteBuffer buffer = ByteBuffer.allocate(16);
            log.debug("read begin...");
            channel.read(buffer, 0, buffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override // read 成功
                public void completed(Integer result, ByteBuffer attachment) {
                    log.debug("Read Complete");
                    attachment.flip();
                    ByteBufferUtil.debugAll(attachment);
                }

                @Override // read 异常
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                }
            });
            log.debug("Read end...");
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        };
    }
}
