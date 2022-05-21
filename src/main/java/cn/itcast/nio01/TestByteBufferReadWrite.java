package cn.itcast.nio01;

import util.ByteBufferUtil;

import java.nio.ByteBuffer;

public class TestByteBufferReadWrite {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put((byte) 0x61); // 'a'
        ByteBufferUtil.debugAll(byteBuffer);

        byteBuffer.put(new byte[]{0x62, 0x63, 0x64}); // 写入b, c, d
        ByteBufferUtil.debugAll(byteBuffer);

        byteBuffer.flip();
        System.out.println(byteBuffer.get());
        ByteBufferUtil.debugAll(byteBuffer);

        byteBuffer.compact();
        ByteBufferUtil.debugAll(byteBuffer);
        byteBuffer.put(new byte[]{0x65, 0x66});
        ByteBufferUtil.debugAll(byteBuffer);
    }
}
