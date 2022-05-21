package cn.itcast.Netty02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
        // 带有Future，Promise的类型一般都是配合异步方法进行使用，用来处理结果
        ChannelFuture channelFuture = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 链接建立后会调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                // 异步非阻塞，main线程发起调用，真正执行连接 connect是 nio 线程
                .connect(new InetSocketAddress("localhost", 8080));

        // 使用sync来同步处理结果
        /*channelFuture.sync(); // 会阻塞，直到nio线程连接建立完毕
        Channel channel = channelFuture.channel();
        channel.writeAndFlush("Hello World");*/

        // 使用addListener(回调对象)方法来异步处理结果

        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            // 在nio线程连接建立好之后，会调用
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                channelFuture.channel().writeAndFlush("Hello World");
            }
        });

    }
}
