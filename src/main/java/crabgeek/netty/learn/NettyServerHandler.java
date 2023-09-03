package crabgeek.netty.learn;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buffer = (ByteBuf) msg;
        byte[] requestBytes = new byte[buffer.readableBytes()];
        buffer.readBytes(requestBytes);

        String request = new String(requestBytes, "UTF-8");
        System.out.println("receive request: " + request);

        String response = "receive request, response from server";
        ByteBuf responseBuf = Unpooled.copiedBuffer(response.getBytes());
        ctx.write(responseBuf);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 真正的发送
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 只要channel打通了，就会执行
        System.out.println("Server is Active......");
    }


}
