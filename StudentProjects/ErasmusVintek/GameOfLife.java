import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class GameOfLife extends Application {

    private static final int CELL_SIZE = 10;
    private static final int GRID_WIDTH = 80;
    private static final int GRID_HEIGHT = 50;

    private boolean[][] grid = new boolean[GRID_HEIGHT][GRID_WIDTH];
    private boolean[][] nextGeneration = new boolean[GRID_HEIGHT][GRID_WIDTH];

    private Canvas canvas;
    private GraphicsContext gc;
    private Timeline timeline;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        canvas = new Canvas(GRID_WIDTH * CELL_SIZE, GRID_HEIGHT * CELL_SIZE);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        initializeGrid();

        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            updateGeneration();
            render();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Game of Life");
        primaryStage.show();
    }

    private void initializeGrid() {
        Random random = new Random();
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                grid[y][x] = random.nextBoolean();
            }
        }
    }

    private void updateGeneration() {
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                int neighbors = countNeighbors(y, x);
                if (grid[y][x]) {
                    nextGeneration[y][x] = (neighbors == 2 || neighbors == 3);
                } else {
                    nextGeneration[y][x] = (neighbors == 3);
                }
            }
        }
        boolean[][] temp = grid;
        grid = nextGeneration;
        nextGeneration = temp;
    }

    private int countNeighbors(int row, int col) {
        int count = 0;
        for (int y = row - 1; y <= row + 1; y++) {
            for (int x = col - 1; x <= col + 1; x++) {
                if (y >= 0 && y < GRID_HEIGHT && x >= 0 && x < GRID_WIDTH && !(y == row && x == col)) {
                    if (grid[y][x]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int y = 0; y < GRID_HEIGHT; y++) {
            for (int x = 0; x < GRID_WIDTH; x++) {
                if (grid[y][x]) {
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

