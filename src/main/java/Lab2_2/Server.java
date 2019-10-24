package Lab2_2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class Server {

    private int port;

    public Server(int port) {
        this.port = port;
    }

    public void init() throws Exception {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap bs = new Bootstrap();
            bs.channel(NioDatagramChannel.class)
                    .group(workerGroup)
                    .handler(new ChannelInitializer<NioDatagramChannel>()
                    {
                        @Override
                        public void initChannel(NioDatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(new PlayerHandler());
                        }
                    });
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

