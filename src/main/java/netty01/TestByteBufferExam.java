package netty01;

import util.ByteBufferUtil;

import java.nio.ByteBuffer;

public class TestByteBufferExam {
    public static void main(String[] args) {
        /*
         网络上有多条数据发送给服务端，数据之间使用\n进行分隔
         但由于某种原因这些数据在接收时，被进行了重新组合，例如原始数据有3条为
         Hello, world\n
         I'm zhangsan\n
         How are you?\n
         变成了下面两个byteBuffer(黏包，半包)
         Hello,world\nI'm zhangsan\nHo
         w are you?\n
         */
        ByteBuffer source = ByteBuffer.allocate(32);
        source.put("Hello,world\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);
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
