package cn.itcast.Netty01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;

/**
 * @program: NettyLearn
 * @description:
 * @author: Guipeng.Xie
 * @create: 2022-05-12 20:35
 */
public class HelloServer {
    public static void main(String[] args) {
        // 1. 服务器端启动器，负责组装netty 组件，协调他们的工作
        new ServerBootstrap()
                // 2. BossEventLoop，WorkerEventLoop，一个selector+thread就是EventLoop
                .group(new NioEventLoopGroup())
                // 3. 选择一个ServerSocketChannel的实现，支持NIO, BIO
                .channel(NioServerSocketChannel.class)
                // 4. Boss 负责处理连接，worker(Child）负责处理读写，决定了worker(child)能执行哪些事
                .childHandler(
                        // 5. channel 代表和客户端进行数据读写的通道，Initializer 初始化，负责添加别的handler
                        new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 6. 添加具体的handler
                        ch.pipeline().addLast(new StringDecoder()); // 将传输过来的ByteBuf转换为字符串
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){ // 自定义的handler
                            // 读事件
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 打印上一步转换好的字符串
                                System.out.println(msg);
                            }
                        });
                    }
                }) // 7. 绑定监听端口
                .bind(8080);
    }
}
