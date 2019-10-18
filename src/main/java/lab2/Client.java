package lab2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class Client {
    private User user;

    public static void main(String[] args) throws InterruptedException {
        Bootstrap b = new Bootstrap(); // (1)
        b.group(new NioEventLoopGroup()); // (2)
        b.channel(NioSocketChannel.class); // (3)
        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
        b.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new ClientHandler());
            }
    });
    ChannelFuture c = b.connect("localhost", 3000).sync();
        c.channel().closeFuture().sync();
    }

}

class ClientHandler extends SimpleChannelInboundHandler<User> {

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext){
        channelHandlerContext.writeAndFlush(new User(new Point(1,1),"ss", new Color(1,1,1)));
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, User in) {
        System.out.println("Client received: " + in.toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
        cause.printStackTrace();
        channelHandlerContext.close();
    }
}