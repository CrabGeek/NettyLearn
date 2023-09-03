package crabgeek.netty.learn;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
    public static void main(String[] args) {
        // 创建两个处理网络的EventLoopGroup
        // Acceptor 线程组
        NioEventLoopGroup parentGroup = new NioEventLoopGroup();
        // Processor 或 Handler 线程组
        NioEventLoopGroup childGroup = new NioEventLoopGroup();

        // 相当于Netty 服务器
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap
                    .group(parentGroup, childGroup)
                    // 监听端口的ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //处理每个连接的 SocketChannel,SocketChannel代表每一个连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    });

            System.out.println("Server 启动了");
            // 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(9001).sync();
            // 等待服务器关闭
            // 同步等待关闭启动服务器的结果
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
