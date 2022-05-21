package cn.itcast.Netty01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-13 14:22
 */
public class EventLoopClient {
    public static void main(String[] args) throws InterruptedException {
        // 创建启动器类
        Channel channel = new Bootstrap()
                // 添加EventLoop
                .group(new NioEventLoopGroup())
                // 选择Channel类型
                .channel(NioSocketChannel.class)
                // 添加处理器
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 链接建立后会调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                }).connect(new InetSocketAddress("localhost", 8080))
                .sync()
                .channel();

        System.out.println(channel);
        System.out.println();

    }
}
