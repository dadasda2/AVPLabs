package lab2;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<String> {
    private static ConcurrentHashMap<String, User> users;
    public String name;

    public ChannelHandlerContext cont;

    public ClientHandler() {
        users = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, User> getUsers(){
        return users;
    }

    public void sendMessage(String msg) {
        cont.writeAndFlush(msg);
    }

    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Registered");
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Active");
        this.cont = ctx;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext arg0, String msg) throws Exception {
        String[] str = msg.split(",");
        System.out.println("msg " + msg);
            if(str[0].equals("FIRST")) {
                name = str[1];
            }

            if (str[0].equals("USERS")) {
                for (int i = 0; i < str.length - 1; i += 6) {
                    Point p = new Point();
                    p.setLocation(Double.parseDouble(str[1 + i]),
                            Double.parseDouble(str[2 + i]));
                    User u = new User(p, str[3 + i]
                            , new Color(Integer.parseInt(str[4 + i]),
                            Integer.parseInt(str[5 + i]),
                            Integer.parseInt(str[6 + i])));
//                System.out.println(u.toString());
                    users.put(u.name, u);

                }
//            System.out.println(users);
            }
            if (str[0].equals("DISC")) {
                users.remove(str[1]);
                System.out.println(str[1] + " deleted");
            }
        }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Inactive");
    }
}