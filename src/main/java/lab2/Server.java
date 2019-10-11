package lab2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {


    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.channel(NioServerSocketChannel.class)
                .group(new NioEventLoopGroup())
                .childHandler(new ChannelInboundHandlerAdapter(){
                    public void channelRead(ChannelHandlerContext cyx, Object msg){
//try {

                        if (msg instanceof ByteBuf) {
//        ((ByteBuf) msg).release();
//        cyx.write(cyx.alloc().buffer().writeBytes("Hello".getBytes()));
                            byte[] helloBytes = "Hello".getBytes();
                            cyx.write(Unpooled.wrappedBuffer(helloBytes));
                            cyx.writeAndFlush(msg);
                        }
//}finally {
//    ReferenceCountUtil.release(msg);
//}
                        System.out.println("msg = " + msg);
                    }
                }).bind(3000).sync();


    }

}
