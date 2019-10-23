package Lab2_2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.io.IOException;

public class PlayerHandler extends SimpleChannelInboundHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object o) throws Exception {
        DatagramPacket data = (DatagramPacket) o;
        ByteBuf byteBuf = data.content();
        String in = byteBuf.toString(CharsetUtil.UTF_8);
        FastDataObject fdo = new FastDataObject(in);//создаём объект с данными
        String action = fdo.getParameter("action");//получаем событие, для которого создадим пакет обработчик
        Packet packet = PacketManager.getPacket(action);//Создаем пакет-обработчик
        System.out.println("Packet " + action + " created");
        packet.setChannel(ctx);//сюда кидаем  объект, с помощью которого будем отправлять обратно
        try {
            packet.handle(fdo);//Обрабатываем пакет, задавая аргументом наши данные
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {//Функция обработок ошибок
        cause.printStackTrace();
    }
}

