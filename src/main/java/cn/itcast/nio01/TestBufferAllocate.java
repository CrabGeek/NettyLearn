package cn.itcast.nio01;

import java.nio.ByteBuffer;

public class TestBufferAllocate {
    public static void main(String[] args) {
        // HeapByteBuffer -Java 堆内存，读写效率低，受到垃圾回收影响
        System.out.println(ByteBuffer.allocate(16).getClass());
        // DirectByteBuffer - 直接内存，读写效率高（少一次数据拷贝），不受到垃圾回收的影响，直接使用操作系统内存，分配效率低（系统调用）
        System.out.println(ByteBuffer.allocateDirect(16).getClass());
    }
}
