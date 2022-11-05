package program.model.game;

import javafx.geometry.Point2D;
import static program.model.enums.CellType.*;
import program.model.enums.CellType;

import java.util.Arrays;
import java.util.Random;



public class MazeCreator {
    public final static int X_ARRAY = 11;
    public final static int Y_ARRAY = 10;
    private static int dfsStartingX, dfsStartingY;
    private static int[][] table;
    private static CellType[][] resultTable;


    static void initializing() {
        table = new int[X_ARRAY + 2][Y_ARRAY + 2];
        resultTable = new CellType[2 * X_ARRAY + 1][2 * Y_ARRAY + 1];

        for (int i = 0; i < 2 * X_ARRAY + 1; i++) {
            for (int j = 0; j < 2 * Y_ARRAY + 1; j++) {
                resultTable[i][j] = WALL;
            }
        }

        for (int i = 1; i <= X_ARRAY; i++) {
            for (int j = 1; j <= Y_ARRAY; j++) {
                resultTable[2 * i - 1][2 * j - 1] = DOT;
            }
        }

        for (int i = 1; i <= X_ARRAY; i++) {
            for (int j = 1; j <= Y_ARRAY; j++) {
                table[i][j] = 1;
            }
        }

    }

    private static void destroyWalls() {
        destroyWallsInColumn(Y_ARRAY);
        destroyWallsInRow(X_ARRAY);
        for (int i = 0; i < 20; i++) {
            Point2D point = getRandomPoint(2 * X_ARRAY - 1 , 2 * Y_ARRAY - 1);
            int x = (int) point.getX();
            int y = (int) point.getY();
            if (resultTable[x][y] != DOT) resultTable[x][y] = DOT;
            else i--;
        }
    }

    private static void destroyWallsInRow(int row) {
        for (int i = 1; i < 2 * Y_ARRAY; i++) {
            resultTable[row][i] = DOT;
        }
    }

    private static void destroyWallsInColumn(int column) {
        for (int i = 1; i < 2 * X_ARRAY; i++) {
            resultTable[i][column] = DOT;
        }
    }

    private static void setPacMan() {
        resultTable[X_ARRAY][Y_ARRAY] = PACMAN;
    }

    static void addEnergyBombs() {
        for (int i = 0; i < 7; i++) {
            Point2D randomPoint = getRandomPoint(2 * X_ARRAY - 1, 2 * Y_ARRAY - 1);
            int x = (int) randomPoint.getX();
            int y = (int) randomPoint.getY();
            if (resultTable[x][y] != ENERGY_BOMB) resultTable[x][y] = ENERGY_BOMB;
            else --i;
        }
    }

    static void getBeginAndEndOfTheMaze() {
        Point2D dfsStartPoint = getRandomPoint(X_ARRAY, Y_ARRAY);
        dfsStartingX = (int) dfsStartPoint.getX();
        dfsStartingY = (int) dfsStartPoint.getY();
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }
    }

    private static Point2D getRandomPoint(int xBound, int yBound) {
        Random random = new Random(System.currentTimeMillis());
        int xArray = (Math.abs(random.nextInt()) % xBound) + 1;
        int yArray = (Math.abs(random.nextInt()) % yBound) + 1;
        return new Point2D(xArray, yArray);
    }

    static void beginDfsFrom(int x, int y) {
        table[x][y] = 2;
        if (table[x + 1][y] != 0 && table[x + 1][y] != 2) {
            resultTable[2 * x][2 * y - 1] = DOT;
            beginDfsFrom(x + 1, y);
        }

        if (table[x][y + 1] != 0 && table[x][y + 1] != 2) {
            resultTable[2 * x - 1][2 * y] = DOT;
            beginDfsFrom(x, y + 1);
        }

        if (table[x][y - 1] != 0 && table[x][y - 1] != 2) {
            resultTable[2 * x - 1][2 * y - 2] = DOT;
            beginDfsFrom(x, y - 1);
        }

        if (table[x - 1][y] != 0 && table[x - 1][y] != 2) {
            resultTable[2 * x - 2][2 * y - 1] = DOT;
            beginDfsFrom(x - 1, y);
        }
    }

    public static CellType[][] creatValidMaze() {
        initializing();
        getBeginAndEndOfTheMaze();
        beginDfsFrom(dfsStartingX, dfsStartingY);
        destroyWalls();
        addEnergyBombs();
        setPacMan();
        setGhosts();
        resultTable[0][1] = WALL;
        resultTable[2 * X_ARRAY][2 * Y_ARRAY - 1] = WALL;
        return resultTable;
    }

    private static void setGhosts() {
        resultTable[1][1] = EMPTY;
        resultTable[1][2 * Y_ARRAY - 1] = EMPTY;
        resultTable[2 * X_ARRAY - 1][1] = EMPTY;
        resultTable[2 * X_ARRAY - 1][2 * Y_ARRAY - 1] = EMPTY;
        resultTable[X_ARRAY][1] = EMPTY;
        resultTable[1][Y_ARRAY] = EMPTY;
    }
}

