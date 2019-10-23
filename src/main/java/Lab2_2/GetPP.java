package Lab2_2;

import java.io.IOException;

public class GetPP extends Packet {
    @Override
    public void write(String outputMessage) throws IOException {//Описываем функцию отправки
        System.out.println(outputMessage);
        getChannelHandlerContext().writeAndFlush(outputMessage);
    }

    @Override
    public void write(FastDataObject fastDataObject) throws IOException {

    }

    @Override
    public void handle(FastDataObject fastDataObject) throws IOException {//Описываем функцию обработки
        long time = Long.parseLong(fastDataObject.getString("t"));//Получаем значение по ключу
        long ping = System.currentTimeMillis()-time;//Вычитаем время из текущего
        System.out.println("Ping: "+ping + " ms");
        System.out.println("Writed");
        write("hello");//Отправляем данные клиенту
    }
}
