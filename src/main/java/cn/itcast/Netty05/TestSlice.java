package cn.itcast.Netty05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-19 21:21
 */
public class TestSlice {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        buffer.writeBytes(new byte[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'});
        TestByteBuf.log(buffer);

        // 在切片过程中，并没有发生数据复制
        ByteBuf s1 = buffer.slice(0, 5);
        ByteBuf s2 = buffer.slice(5, 5);

        TestByteBuf.log(s1);
        TestByteBuf.log(s2);
    }


}
