package lab2.lec7;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Main {

    public static void main(String[] args) {

        new ServerBootstrap().group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
//                        new FixedLengthFrameDecoder(5),
//                        new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n", StandardCharsets.UTF_8)),
                        new LoggingHandler(LogLevel.INFO),
                        new HttpServerCodec(),
                        new ChunkedWriteHandler(),
//                        new StringDecoder(StandardCharsets.UTF_8),
//                        new StringEncoder(StandardCharsets.UTF_8),
                        new MyHandler(MyHandlers.handlers)
                );
            }
        }).bind(3000);

    }
}

class MyHandler extends SimpleChannelInboundHandler<HttpRequest> {

    Map<String, Function<HttpRequest, Object>> handlers;

    public MyHandler(Map<String, Function<HttpRequest, Object>> handlers) {
        this.handlers = handlers;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpRequest msg) throws Exception {

        System.out.println("msg = " + msg.uri());

        QueryStringDecoder query = new QueryStringDecoder(msg.uri(), StandardCharsets.UTF_8);

        String path = query.path();

        System.out.println("path = " + path);

        Function<HttpRequest, Object> handler = handlers.get(path);

        if (handler == null) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.copiedBuffer("Not found", StandardCharsets.UTF_8)))
                    .addListeners(ChannelFutureListener.CLOSE);
            return;

        }


        Object result = handler.apply(msg);

        if (result instanceof HttpResponse) {
            ctx.writeAndFlush(result);
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT).addListeners(ChannelFutureListener.CLOSE);
        } else if (result instanceof String) {
            ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                    Unpooled.copiedBuffer((String) result, StandardCharsets.UTF_8)));
            ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT).addListeners(ChannelFutureListener.CLOSE);
        } else if (result instanceof CompletableFuture<?>) {

            CompletableFuture<?> future = (CompletableFuture<?>) result;

            future.handle((r, ex) -> {
                ctx.writeAndFlush(new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
                        Unpooled.copiedBuffer((String) r, StandardCharsets.UTF_8))).addListeners(ChannelFutureListener.CLOSE);
                return null;
            });

        }

    }


}

