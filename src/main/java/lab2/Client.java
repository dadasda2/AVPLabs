package lab2;
////package lab2;
////
////import io.netty.bootstrap.Bootstrap;
////import io.netty.buffer.ByteBuf;
////import io.netty.buffer.Unpooled;
////import io.netty.channel.ChannelInboundHandlerAdapter;
////import io.netty.channel.ChannelInitializer;
////import io.netty.channel.ChannelOption;
////import io.netty.channel.nio.NioEventLoopGroup;
////import io.netty.channel.socket.SocketChannel;
////import io.netty.channel.*;
////import io.netty.channel.socket.nio.NioSocketChannel;
////import io.netty.handler.codec.string.StringDecoder;
////import io.netty.handler.codec.string.StringEncoder;
////import io.netty.handler.logging.LoggingHandler;
////import io.netty.util.CharsetUtil;
////
////import java.awt.*;
////import java.awt.geom.Point2D;
////import java.net.UnknownServiceException;
////import java.nio.charset.StandardCharsets;
////import java.util.List;
////
////public class Client {
////    private User user;
////
////    public static void main(String[] args) throws InterruptedException {
////        Bootstrap b = new Bootstrap(); // (1)
////        b.group(new NioEventLoopGroup()); // (2)
////        b.channel(NioSocketChannel.class); // (3)
////        b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
////        b.handler(new ChannelInitializer<SocketChannel>() {
////            @Override
////            public void initChannel(SocketChannel ch) throws Exception {
////
////                ch.pipeline().addLast(
////                        new LoggingHandler(),
////                        new StringDecoder(StandardCharsets.UTF_8),
////                        new StringEncoder(StandardCharsets.UTF_8),
////                        new ClientHandler()
////                        );
////            }
////    });
////    ChannelFuture c = b.connect("localhost", 3000).sync();
////    c.channel().closeFuture().sync();
////
////    }
////
////}
////
////class ClientHandler extends SimpleChannelInboundHandler<String>{
////    private ChannelHandlerContext ctx;
////
////    public void sendMessage(String msg){
////        ChannelFuture cf = ctx.write(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
////        ctx.flush();
////        if (!cf.isSuccess()) {
////            System.out.println("Send failed: " + cf.cause());
////        }
////
////    }
////
////    @Override
////    public void channelActive(ChannelHandlerContext channelHandlerContext){
////        ctx = channelHandlerContext;
////    }
////
////    @Override
////    public void channelRead0(ChannelHandlerContext channelHandlerContext, String in) {
////        System.out.println("Client received: " + in);
////    }
////
////    @Override
////    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable cause){
////        cause.printStackTrace();
////        channelHandlerContext.close();
////    }
////}
//
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.*;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import io.netty.handler.codec.DelimiterBasedFrameDecoder;
//import io.netty.handler.codec.Delimiters;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//import io.netty.handler.logging.LoggingHandler;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//public class Client implements Runnable {
//
//    String host = "localhost";
//    int port = 3000;
//    private final ClientHandler clientHandler = new ClientHandler();
//    private boolean isRunning = false;
//    private ExecutorService executor = null;
//
//
//    public static void main(String[] args) throws InterruptedException {
//        Client client = new Client();
//        client.startClient();
//        Thread.sleep(10000);
//        client.writeMessage("random_text" );
//
//        //client.stopClient();  //call this at some point to shutdown the client
//    }
//
//    public synchronized void startClient() {
//        if (!isRunning) {
//            executor = Executors.newFixedThreadPool(1);
//            executor.execute(this);
//            isRunning = true;
//        }
//    }
//
//    public synchronized boolean stopClient() {
//        boolean bReturn = true;
//        if (isRunning) {
//            if (executor != null) {
//                executor.shutdown();
//                try {
//                    executor.shutdownNow();
//                    if (executor.awaitTermination(calcTime(10, 0.66667), TimeUnit.SECONDS)) {
//                        if (!executor.awaitTermination(calcTime(10, 0.33334), TimeUnit.SECONDS)) {
//                            bReturn = false;
//                        }
//                    }
//                } catch (InterruptedException ie) {
//                    executor.shutdownNow();
//                    Thread.currentThread().interrupt();
//                } finally {
//                    executor = null;
//                }
//            }
//            isRunning = false;
//        }
//        return bReturn;
//    }
//
//    private long calcTime(int nTime, double dValue) {
//        return (long) ((double) nTime * dValue);
//    }
//
//    @Override
//    public void run() {
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            Bootstrap b = new Bootstrap();
//            b.group(workerGroup);
//            b.channel(NioSocketChannel.class);
//            b.option(ChannelOption.SO_KEEPALIVE, true);
//            b.handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                public void initChannel(SocketChannel ch) throws Exception {
//                    ChannelPipeline pipeline = ch.pipeline();
//                    pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
//                    pipeline.addLast(new LoggingHandler());
//                    pipeline.addLast(new StringDecoder());
//                    pipeline.addLast(new StringEncoder());
//                    pipeline.addLast(clientHandler);
//                }
//            });
//
//            ChannelFuture f = b.connect(host, port).sync();
//
//            f.channel().closeFuture().sync();
//        } catch (InterruptedException ex) {
//            // do nothing
//        } finally {
//            workerGroup.shutdownGracefully();
//        }
//    }
//
//    public void writeMessage(String msg) {
//        clientHandler.sendMessage(msg);
//    }
//}

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.StandardCharsets;

public class Client{

    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup());
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel channel) throws Exception {
                channel.pipeline().addLast(
                        new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n", StandardCharsets.UTF_8)),
                        new StringDecoder(StandardCharsets.UTF_8),
                        new StringEncoder(StandardCharsets.UTF_8),
                        new LoggingHandler(),
                        new ClientHandler()
                );
            }
        });
        ChannelFuture cf = bootstrap.connect("localhost", 3000).sync();
        cf.channel().closeFuture().sync();
    }
}

