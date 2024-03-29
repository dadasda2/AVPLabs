package lab2;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.ChannelHandlerContext;
import  lab2.maze.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MyMaze extends JPanel {
    private static final long serialVersionUID = -5594533691085748251L;
    private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();
    public TileMaze tileMaze;
    /**
     * Maze width.
     */
    private static final int WIDTH = 10;
    /**
     * Maze height.
     */
    private static final int HEIGHT = 10;
    /**
     * Maze path width.
     */
    private static final int N = 30;

    private Dimension dimension;
    private List<Shape> shapes;

    public MyMaze() {
        dimension = new Dimension();
        shapes = new ArrayList<Shape>();
    }

    public void setUsers(ConcurrentHashMap<String, User> u) {
        users = u;
    }

    @Override
    public Dimension getPreferredSize() {
        return dimension;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        for (Shape s : shapes) {
            g2d.draw(s);
            g2d.fill(s);
        }
        for (String us : users.keySet()) {
            g2d.setColor(users.get(us).color);
            int x = (int) users.get(us).point.getX();
            int y = (int) users.get(us).point.getY();


//            g2d.drawRect(x, y, 10, 10);
            g2d.fillRect(x * N,y * N,N,N);
        }
    }

    public void loadTileMaze() {
        shapes.clear();
        tileMaze = new TileMaze(new RecursiveBacktracker(WIDTH, HEIGHT));
        tileMaze.generate();
        for (int y = 0; y < tileMaze.getHeight(); ++y) {
            for (int x = 0; x < tileMaze.getWidth(); ++x) {
                if (tileMaze.isWall(x, y)) {
                    shapes.add(new Rectangle(x * N, y * N, N, N));
                }
            }
        }
        int panelWidth = tileMaze.getWidth() * N + 1;
        int panelHeight = tileMaze.getHeight() * N + 1;
        dimension.setSize(panelWidth, panelHeight);
    }


}
