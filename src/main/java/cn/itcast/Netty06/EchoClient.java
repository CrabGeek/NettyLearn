package cn.itcast.Netty06;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-20 15:54
 */
@Slf4j
public class EchoClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                log.debug("Receive from server {}",buf.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                }).connect("localhost", 8180);

        Channel channel = channelFuture.channel();
        channelFuture.addListener(future -> {
            new Thread(()->{
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    String s = scanner.nextLine();
                    if (s.equals("q")) {
                        channel.close();
                        break;
                    }
                    channel.writeAndFlush(s);
                }
            }).start();
        });

        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.addListener(future -> {
            group.shutdownGracefully();
        });
    }
}
