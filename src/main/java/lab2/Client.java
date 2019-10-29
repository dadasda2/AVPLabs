package lab2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.*;

public class Client {
    private ClientHandler clientHandler;
    private MyMaze maze = new MyMaze();

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
            clientHandler = new ClientHandler();
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
                window.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent keyEvent) {
                    }

                    @Override
                    public void keyPressed(KeyEvent keyEvent) {
                        User user = clientHandler.getUsers().get(clientHandler.name);
                        int userX = user.point.x;
                        int userY = user.point.y;
                        if (keyEvent.getExtendedKeyCode() == 37) {
                            if (!maze.tileMaze.isWall(userX - 1, userY)){
                                userX--;
                                clientHandler.sendMessage("POINT," + userX + "," + userY + "\n");
                            }
                        }
                        if (keyEvent.getExtendedKeyCode() == 38) {
                            if (!maze.tileMaze.isWall(userX, userY - 1)) {
                                userY--;
                                clientHandler.sendMessage("POINT," + userX + "," + userY + "\n");
                            }
                        }
                        if(keyEvent.getExtendedKeyCode() == 39) {
                            if (!maze.tileMaze.isWall(userX + 1, userY)) {
                                userX++;
                                clientHandler.sendMessage("POINT," + userX + "," + userY + "\n");
                            }
                        }
                            if (keyEvent.getExtendedKeyCode() == 40) {
                                if (!maze.tileMaze.isWall(userX, userY + 1)) {
                                    userY++;
                                    clientHandler.sendMessage("POINT," + userX + "," + userY + "\n");
                                }
                            }
                        }

                    @Override
                    public void keyReleased(KeyEvent keyEvent) {

                    }
                });
                window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                window.setLayout(new BorderLayout());
                window.add(maze, BorderLayout.CENTER);
                window.pack();
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
