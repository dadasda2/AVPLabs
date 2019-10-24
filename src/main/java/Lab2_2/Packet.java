package Lab2_2;

import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;

public abstract class Packet {
    private PlayerHandler player;
    private ChannelHandlerContext channelHandlerContext;

    public PlayerHandler getPlayer() {
        return player;
    }

    public void setPlayer(PlayerHandler player) {
        this.player = player;
    }

    public abstract void write(FastDataObject fastDataObject) throws IOException;//в дальнейшем отправка сообщения клиенту(ам)

    public abstract void handle(FastDataObject fastDataObject) throws IOException;//обработка сообщения

    public void setChannel(ChannelHandlerContext channel) {
        channelHandlerContext = channel;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }
}
