package netty01;

import util.ByteBufferUtil;

import java.nio.ByteBuffer;

public class TestByteBufferRead {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put(new byte[]{'a', 'b', 'c', 'd'});
        byteBuffer.flip();

        // rewind 从头开始读
//        byteBuffer.get(new byte[4]);
//        ByteBufferUtil.debugAll(byteBuffer);
//        byteBuffer.rewind();
//        System.out.println((char) byteBuffer.get());

        // mark && reset
        // mark 做一个标记，记录position的位置，reset是将position重置到mark的位置上
//        System.out.println((char) byteBuffer.get());
//        System.out.println((char) byteBuffer.get());
//        byteBuffer.mark(); // 添加标记，索引为2的位置
//        System.out.println((char) byteBuffer.get());
//        System.out.println((char) byteBuffer.get());
//        byteBuffer.reset(); // 将position重置到索引2
//        System.out.println((char) byteBuffer.get());
//        System.out.println((char) byteBuffer.get());

        // get(i) 不会改变position 索引的位置
        System.out.println((char) byteBuffer.get(3));
        ByteBufferUtil.debugAll(byteBuffer);

    }
}
