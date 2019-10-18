package lab2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static ConcurrentHashMap<ChannelHandlerContext, User> users = new ConcurrentHashMap<>();
    public static void main(String[] args){
        new ServerBootstrap().group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n", StandardCharsets.UTF_8)),
                        new StringDecoder(StandardCharsets.UTF_8),
                        new StringEncoder(StandardCharsets.UTF_8),
                        new LoggingHandler(LogLevel.INFO),
                        new Handler(users)
                );
            }
        }).bind(3000);

    }

}

