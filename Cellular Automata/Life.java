import javax.swing.*;
import java.awt.*;

public class Life {

    public static final int CELL_SIZE = 5;

    private Grid grid;

    public Life(int size, int iterations, String initialization) {
        JFrame frame = new JFrame("Life");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(size * CELL_SIZE, size * CELL_SIZE);

        grid = new Grid(size, iterations);
        frame.add(grid);
        if (initialization.equals("R")) {
            grid.initCellsWithRandomPattern();
        } else if (initialization.equals("G")) {
            grid.initCellsWithGosperGliderGum();
        } else {
            exit("Cells should be initialized with either a Random (R) pattern or a Gosper (G) glider gun.");
        }

        frame.setVisible(true);
    }

    public void start() {
        Thread thread = new Thread(grid);
        thread.start();
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            exit("Wrong argument number, should be 3.");
        }

        int size = Integer.parseInt(args[0]);
        int iterations = Integer.parseInt(args[1]);
        String initialization = args[2];

        Life life = new Life(size, iterations, initialization);
        life.start();
    }

    public static void exit(String message) {
        System.err.println(message);
        System.exit(-1);
    }
}

class Grid extends JPanel implements Runnable {

    private static final int DELAY = 100;
    private static final double PROBABILITY = 0.8;
    private static final int[][] GOSPER_GLIDER_GUM = new int[][]{
            {1, 25}, {2, 23}, {2, 25}, {3, 13}, {3, 14}, {3, 21},
            {3, 22}, {3, 35}, {3, 36}, {4, 12}, {4, 16}, {4, 21},
            {4, 22}, {4, 35}, {4, 36}, {5, 1}, {5, 2}, {5, 11},
            {5, 17}, {5, 21}, {5, 22}, {6, 1}, {6, 2}, {6, 11},
            {6, 15}, {6, 17}, {6, 18}, {6, 23}, {6, 25}, {7, 11},
            {7, 17}, {7, 25}, {8, 12}, {8, 16}, {9, 13}, {9, 14},
    };

    private int size;
    private int iterations;
    private boolean[][] cells;

    public Grid(int size, int iterations) {
        this.size = size;
        this.iterations = iterations;
        this.cells = new boolean[size][size];
    }

    public void initCellsWithRandomPattern() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = Math.random() >= PROBABILITY;
            }
        }
    }

    public void initCellsWithGosperGliderGum() {
        for (int[] position : GOSPER_GLIDER_GUM) {
            if (position[0] < size && position[1] < size) {
                cells[position[0]][position[1]] = true;
            }
        }
    }

    @Override
    public void run() {
        for (int i = 0; i < iterations; i++) {
            updateGrid();

            try {
                Thread.sleep(DELAY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            updateCells();
        }
    }

    private void updateGrid() {
        Graphics g = getGraphics();
        g.setColor(Color.BLACK);
        update(g);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j]) {
                    g.fillRect(j * Life.CELL_SIZE, i * Life.CELL_SIZE, Life.CELL_SIZE, Life.CELL_SIZE);
                }
            }
        }
    }

    private void updateCells() {
        int[][] neighbors = new int[][]{
                {1, 1}, {1, 0}, {1, -1}, {0, 1},
                {0, -1}, {-1, 1}, {-1, 0}, {-1, -1},
        };

        boolean[][] newCells = new boolean[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int count = 0;
                for (int[] neighbor : neighbors) {
                    int x = i + neighbor[0];
                    int y = j + neighbor[1];
                    if (x >= 0 && x < size && y >= 0 && y < size && cells[x][y]) {
                        count++;
                    }
                }

                if (cells[i][j]) {
                    newCells[i][j] = count == 2 || count == 3;
                } else {
                    newCells[i][j] = count == 3;
                }

            }
        }
        this.cells = newCells;
    }
}