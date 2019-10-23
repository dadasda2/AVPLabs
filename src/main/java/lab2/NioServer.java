package lab2;

import java.beans.beancontext.BeanContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;



public class NioServer {

    public static void main(String[] args) throws IOException {
//        ByteBuffer buffer = ByteBuffer.allocate(16);
//        buffer.putInt(345678);
//        System.out.println(buffer);
//        buffer.putLong(3);
//        System.out.println(buffer);
//
//        buffer.flip();
//        System.out.println(buffer);
//
//        write(buffer);
//        System.out.println(buffer);
//
//        buffer.compact();
//        System.out.println(buffer);
//
////        buffer.order(ByteOrder.LITTLE_ENDIAN); С‡С‚РµРЅРёРµ С‡РёСЃРµР» СЃ РјР»Р°РґС€РµРіРѕ СЂР°Р·СЂСЏРґР°
//
//        ByteBuffer buf = ByteBuffer.allocateDirect(16);
//        System.out.println(buf);

        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(3000));
        serverSocketChannel.configureBlocking(false);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

//        List<SocketChannel> socketChannels = new CopyOnWriteArrayList<>();

        while (true) {
            int select = selector.select();
            if(select == 0) continue;

            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            for (SelectionKey key : selectionKeys) {
                SelectableChannel channel = key.channel();
                if(channel instanceof ServerSocketChannel){
                    SocketChannel socketChannel = ((ServerSocketChannel) channel).accept();
                    if(socketChannel != null) {
                        socketChannel.configureBlocking(false);// true - Р·Р°Р±Р»РѕРєРёСЂСѓРµС‚ РїРѕРєР° РґР°РЅРЅС‹Рµ РїСЂРёРґСѓС‚, false - РЅРµ Р¶РґРµС‚С‚, СЃСЂР°Р·Сѓ Р·Р°РІРµСЂС€Р°С‚РµСЃСЏ
//                socketChannels.add(socketChannel);
                        socketChannel.register(selector,SelectionKey.OP_READ);
                    }

                }
                if(channel instanceof SocketChannel){
                    ByteBuffer buf = ByteBuffer.allocate(128);
                    int read =   ((SocketChannel)channel).read(buf);
                    if(read == -1){
                        channel.close();
                    }
                    if(read == 0) continue;
                    buf.flip();
                    System.out.println("after read buf = " + buf);
                    String s = new String(buf.array(), buf.position(), buf.remaining());
                    System.out.println(s);
                    buf.clear();
                }
            }
            selectionKeys.clear();

//            SocketChannel socketChannel = serverSocketChannel.accept();
//
//            if(socketChannel != null) {
//                socketChannel.configureBlocking(false);// true - Р·Р°Р±Р»РѕРєРёСЂСѓРµС‚ РїРѕРєР° РґР°РЅРЅС‹Рµ РїСЂРёРґСѓС‚, false - РЅРµ Р¶РґРµС‚С‚, СЃСЂР°Р·Сѓ Р·Р°РІРµСЂС€Р°С‚РµСЃСЏ
////                socketChannels.add(socketChannel);
//                socketChannel.register(selector,SelectionKey.OP_READ);
//            }
//            for (SocketChannel channel:socketChannels){
//
//
//                ByteBuffer buf = ByteBuffer.allocate(1024);
//
////            while (socketChannel.isOpen()) {
//                int read =   channel.read(buf);
//              if(read == -1){
//                  socketChannels.remove(channel);
//              }
//              if(read == 0) continue;
//
//                buf.flip();
//                System.out.println("after read buf = " + buf);
//                String s = new String(buf.array(), buf.position(), buf.remaining());
//
//                System.out.println(s);
//                buf.clear();
//            }
//            }
//        }
//    private static void write(ByteBuffer buffer) {
//        System.out.println("int = " + buffer.getInt());
//        System.out.println("long = " + buffer.getLong());


        }
    }
}