package cn.itcast.Netty05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-19 14:01
 */
public class TestByteBuf {
    public static void main(String[] args) {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        System.out.println(buffer);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            stringBuilder.append("a");
        }
        buffer.writeBytes(stringBuilder.toString().getBytes());
        buffer.release();
        log(buffer);
    }

    public static void log (ByteBuf buf) {
        int length = buf.readableBytes();
        int rows =length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
        StringBuilder builder = new StringBuilder(rows * 80 * 2)
                .append("read index: ").append(buf.readerIndex())
                .append(" write index: ").append(buf.writerIndex())
                .append(" capacity: ").append(buf.capacity())
                .append(NEWLINE);
        appendPrettyHexDump(builder, buf);
        System.out.println(builder.toString());


    }
}
