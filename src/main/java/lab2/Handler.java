package lab2;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Handler extends SimpleChannelInboundHandler<String> {
    private User user;
    private boolean isFirst;
    private static ConcurrentHashMap<ChannelHandlerContext, User> users = new ConcurrentHashMap<>();


    public Handler() {
        user = new User();
        isFirst = true;
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
        System.out.println("inp " + inp);
        if (isFirst) {
            Random rnd = new Random();
            user.name = inp;
            user.color = new Color(rnd.nextInt(255), rnd.nextInt(255), rnd.nextInt(255));
            users.put(ctx, user);
            isFirst = false;

            SendUsers();

        }
        if(inp.equals("EXIT")){
            System.out.println(user.name + "Exiting...\n");
            for (ChannelHandlerContext us: users.keySet()) {
                System.out.println(us);
                us.executor().submit(()->{
                   us.writeAndFlush("DISC," + users.get(ctx).name +"\n");
                });
            }
            users.remove(ctx);
            ctx.close();
        }

        String[] point = inp.split(",");
        if(point[0].equals("POINT")){
            user.point.x = Integer.parseInt(point[1]);
            user.point.y = Integer.parseInt(point[2]);
            users.put(ctx, user);
            SendUsers();

        }


    }

    private void SendUsers() {
        StringBuilder res = new StringBuilder("USERS");
        for (ChannelHandlerContext us: users.keySet()) {
            res.append(",").append(users.get(us).genToString());
        }

        for (ChannelHandlerContext us: users.keySet()) {
            String finalRes = res.toString();
            us.executor().submit(()->{
                us.writeAndFlush(finalRes + "\n");
            });
        }
    }
}
