package lab2;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lab2.maze.*;

public class Labyrinth extends JPanel {

    private static int WIDTH;
    private static int HEIGHT;
    private static int N;

    private Dimension dimension;
    private List<Shape> shapes;

    public Labyrinth(int width, int height, int n) {
        dimension = new Dimension();
        shapes = new ArrayList<Shape>();
        WIDTH = width;
        HEIGHT = height;
        N = n;
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
    }

    public void loadMaze() {
        shapes.clear();
        Maze maze = new RandomizedKruskals(WIDTH, HEIGHT);
        maze.generate();
        for (int x = 0; x < maze.getWidth(); ++x) {
            if (maze.isWall(x, 0, Direction.NORTH)) {
                int x1 = x * N;
                int x2 = x1 + N;
                shapes.add(new Line2D.Float(x1, 0, x2, 0));
            }
        }
        for (int y = 0; y < maze.getHeight(); ++y) {
            if (maze.isWall(0, y, Direction.WEST)) {
                int y1 = y * N;
                int y2 = y1 + N;
                shapes.add(new Line2D.Float(0, y1, 0, y2));
            }
        }
        for (int y = 0; y < maze.getHeight(); ++y) {
            for (int x = 0; x < maze.getWidth(); ++x) {
                if (maze.isWall(x, y, Direction.SOUTH)) {
                    int x1 = x * N;
                    int x2 = x1 + N;
                    int y1 = (y + 1) * N;
                    shapes.add(new Line2D.Float(x1, y1, x2, y1));
                }
                if (maze.isWall(x, y, Direction.EAST)) {
                    int x1 = (x + 1) * N;
                    int y1 = y * N;
                    int y2 = y1 + N;
                    shapes.add(new Line2D.Float(x1, y1, x1, y2));
                }
            }
        }
        int panelWidth = maze.getWidth() * N + 1;
        int panelHeight = maze.getHeight() * N + 1;
        dimension.setSize(panelWidth, panelHeight);
    }

    public void loadTileMaze() {
        shapes.clear();
        TileMaze tileMaze = new TileMaze(new RecursiveBacktracker(WIDTH, HEIGHT));
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

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            final int ai = i*50;
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    Labyrinth panel = new Labyrinth(60, 20, 2);
//                panel.loadMaze();
                    panel.loadTileMaze();
                    JFrame window = new JFrame("Maze Example");
                    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    window.add(panel);
                    window.pack();
                    window.setLocationRelativeTo(null);
                    window.setLocation(ai,500);
                    window.setVisible(true);
                }
            });
        }
    }
}
