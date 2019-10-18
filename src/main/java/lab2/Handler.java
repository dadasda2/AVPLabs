package lab2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Handler extends SimpleChannelInboundHandler<String> {
    private User user;
    private boolean isFirst;
    private static ConcurrentHashMap<ChannelHandlerContext, User> users = new ConcurrentHashMap<>();

    public Handler(ConcurrentHashMap<ChannelHandlerContext, User> u) {
        user = new User();
        isFirst = true;
        users = u;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        users.remove(ctx);
        super.channelUnregistered(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String inp) {
        if (isFirst) {
            Random rnd = new Random();
            System.out.println("inp " + inp);
            user.name = inp;
            user.color = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
            users.put(ctx, user);
            isFirst = false;

            String res = "";
            for (ChannelHandlerContext us: users.keySet()) {
                res +="," + users.get(us).genToString();
            }
            res = res.substring(1);

                for (ChannelHandlerContext us: users.keySet()) {
                    String finalRes = res;
                    us.executor().submit(()->{
                        us.writeAndFlush(finalRes + "\n");
                    });
            }

        }
        if(inp.equals("EXIT")){
            System.out.println("Exiting...\n");
            try {
                this.channelUnregistered(ctx);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String[] point = inp.split(",");
        if(point[0].equals("POINT")){
            user.point.x = Integer.parseInt(point[1]);
            user.point.y = Integer.parseInt(point[2]);
        }

    }
}
