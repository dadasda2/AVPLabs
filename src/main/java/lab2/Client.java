package lab2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.EventExecutor;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.*;
//
//public class Client implements Runnable {
//    private boolean isRunning = false;
//    private ExecutorService executor = null;
//    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
//    public ClientHandler clientHandler;
//    private static MyMaze maze;
//
//
//    public static void main(String[] args) throws InterruptedException {
//        String[] heroes = {"Adam", "Eve", "Steve", "Nikolai"};
//        for (String str : heroes
//        ) {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    maze = new MyMaze(users);
//                    maze.loadTileMaze();
//                    Client client = new Client();
//                    client.clientHandler = new ClientHandler(users,maze);
//                    client.start();
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    Random rnd = new Random();
//                    client.writeMessage(str + "\n");
//                    client.writeMessage("POINT,"+ rnd.nextInt(400) +","+ rnd.nextInt(400) +"\n");
//
//                }
//            }).start();
//
//        }
//    }
//
//    public synchronized void start() {
//        if (!isRunning) -
//            isRunning = true;
//        }
//    }
//
//    public synchronized void stopClient() {
//        boolean bReturn = true;
//        if (isRunning) {
//            System.out.println("EXITING....");
//            writeMessage("EXIT\n");
//            if (executor != null) {
//                executor.shutdown();
//                try {
//                    executor.shutdownNow();
//                } finally {
//                    executor = null;
//                }
//            }
//            isRunning = false;
//        }
//    }
//
//
//    public void writeMessage(String msg) {
//        clientHandler.sendMessage(msg);
//    }
//
//    @Override
//    public void run() {
//        Bootstrap bootstrap = new Bootstrap();
//        bootstrap.group(new NioEventLoopGroup());
//        bootstrap.channel(NioSocketChannel.class);
//        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
//            @Override
//            protected void initChannel(NioSocketChannel channel) throws Exception {
//                channel.pipeline().addLast(
//                        new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n", StandardCharsets.UTF_8)),
//                        new StringDecoder(StandardCharsets.UTF_8),
//                        new StringEncoder(StandardCharsets.UTF_8),
//                        new LoggingHandler(),
//                        clientHandler
//                );
//            }
//        });
//
//        ChannelFuture f = null;
//        try {
//            f = bootstrap.connect("localhost", 3000).sync();
//            f.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//}
//


public class Client {
    private ClientHandler clientHandler = new ClientHandler();
    private MyMaze maze = new MyMaze();
    private Listener listener = new Listener();

    public static void main(String[] args) {
        String[] heroes = {"Adam", "Eve", "Steve", "Nikolai"};
        System.out.println("run");
        for (String str : heroes
        ) {
            new Thread(()-> {
                    Client client = new Client();
                    client.clientRun(str + "\n");

            }).start();
        }
}

        public void clientRun (final String name){
            ExecutorService executor = new NioEventLoopGroup();
            executor = Executors.newFixedThreadPool(10);
            ExecutorService finalExecutor = executor;
            executor.execute(() -> {

                Bootstrap bootstrap = new Bootstrap();
                bootstrap.group(new NioEventLoopGroup());
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline().addLast(
                                new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("\n", StandardCharsets.UTF_8)),
                                new StringDecoder(StandardCharsets.UTF_8),
                                new StringEncoder(StandardCharsets.UTF_8),
                                new LoggingHandler(),
                                clientHandler
                        );
                    }
                });

                ChannelFuture f = null;
                try {
                    f = bootstrap.connect("localhost", 3000).sync();
                    Thread.sleep(1000);
                    clientHandler.sendMessage(name);
                    f.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

            executor.execute(() -> {
                maze = new MyMaze();
                maze.loadTileMaze();
                maze.setUsers(clientHandler.getUsers());
                JFrame window = new JFrame("Maze Example");
                window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                window.setLayout(new BorderLayout());
                window.add(maze, BorderLayout.CENTER);
                window.setSize(400, 400);
                window.setLocationRelativeTo(null);
                window.setVisible(true);

                finalExecutor.execute(() -> {
                    while (true){
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        maze.setUsers(clientHandler.getUsers());
                        window.add(maze, BorderLayout.CENTER);
                        window.repaint();
                }});
            });
        }

    }
