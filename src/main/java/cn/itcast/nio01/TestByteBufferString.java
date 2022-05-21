package cn.itcast.nio01;

import util.ByteBufferUtil;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

public class TestByteBufferString {
    public static void main(String[] args) {
        //1. 字符串转为ByteBuffer
        String hello = "hello";
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.put(hello.getBytes());
        ByteBufferUtil.debugAll(buffer);

        //2. Charset
        ByteBuffer encode = StandardCharsets.UTF_8.encode(hello);
        ByteBufferUtil.debugAll(encode);

        // 3. wrap
        ByteBuffer wrap = ByteBuffer.wrap(hello.getBytes());
        ByteBufferUtil.debugAll(wrap);


        // ByteBuffer转字符串
        CharBuffer decode = StandardCharsets.UTF_8.decode(encode);
        System.out.println(decode.toString());

        buffer.flip();
        CharBuffer decode2 = StandardCharsets.UTF_8.decode(buffer);
        System.out.println(decode2.toString());
    }
}
