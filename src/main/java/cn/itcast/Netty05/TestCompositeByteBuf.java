package cn.itcast.Netty05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-19 22:17
 */
public class TestCompositeByteBuf {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buffer.getClass());
        buffer.writeBytes(new byte[] {1, 2, 3, 4, 5});

        ByteBuf buffer1 = ByteBufAllocator.DEFAULT.buffer();
        buffer1.writeBytes(new byte[]{6, 7, 8, 9, 10});

        // 将多个ByteBuf组合成一个byteBuf（零拷贝，在逻辑上的组合)
        CompositeByteBuf byteBufs = ByteBufAllocator.DEFAULT.compositeBuffer();
        byteBufs.addComponents(true, buffer, buffer1);

        TestByteBuf.log(byteBufs);
    }
}
