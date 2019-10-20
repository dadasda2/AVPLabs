package lab2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) throws InterruptedException {

        magic();

    }

public static void magic(){
    Bootstrap b = new Bootstrap(); // (1)
    b.group(new NioEventLoopGroup()); // (2)
    b.channel(NioSocketChannel.class); // (3)
    b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
    b.handler(new ChannelInitializer<SocketChannel>() {
        @Override
        public void initChannel(SocketChannel ch) throws Exception {

            ch.pipeline().addLast(
                    new LoggingHandler(),
                    new StringDecoder(StandardCharsets.UTF_8),
                    new StringEncoder(StandardCharsets.UTF_8),
                    new ClientHandler()
            );
        }
    });
    ChannelFuture c = null;
    try {
        c = b.connect("localhost", 3000).sync();
        c.channel().closeFuture().sync();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
}

class ClientHandler extends SimpleChannelInboundHandler<String>{
    private ChannelHandlerContext ctx;
    private User user;
    private static MyMaze maze;

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext){
        ctx = channelHandlerContext;
        ctx.writeAndFlush("qwerty\n");
    }

    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, String inp) {
        String[] input = inp.split(",");
        if(input[0].equals("USERS")){
            System.out.println(inp);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
        cause.printStackTrace();
        channelHandlerContext.close();
    }
}

