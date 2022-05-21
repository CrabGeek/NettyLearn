package cn.itcast.Netty02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Scanner;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-15 17:20
 */
@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group= new NioEventLoopGroup(1);
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override // 链接建立后会调用
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast(new StringEncoder());
                    }
                }).connect("localhost", 8080);

        // 同步处理
/*        channelFuture.sync();
        Channel channel = channelFuture.channel();
        log.debug("{}", channel);
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                if (line.equals("q")) {
                    channel.close(); // 异步操作
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();*/

        // 异步处理
        Channel channel = channelFuture.channel();
        channelFuture.addListener((ChannelFutureListener) future -> {
            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String line = scanner.nextLine();
                    if (line.equals("q")) {
                        channel.close(); // 异步操作
                        break;
                    }
                    channel.writeAndFlush(line);
                }
            }, "input").start();
        });

        // 获取ClosedFuture对象，1.同步处理关闭，2. 异步处理关闭
        ChannelFuture closeFuture = channel.closeFuture();
        /*System.out.println("Waiting close...");
        closeFuture.sync(); // 同步处理
        log.debug("处理关闭之后的操作");*/
        // 异步调用
        closeFuture.addListener((ChannelFutureListener) future -> {
            log.debug("处理关闭之后的操作");
            group.shutdownGracefully();
        });

    }
}
