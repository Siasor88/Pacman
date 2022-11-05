package program.model.game;

import com.google.gson.annotations.Expose;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import program.Main;
import program.controller.SettingController;
import program.model.enums.CellType;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static program.model.enums.CellType.*;
import static program.model.game.Ghost.MAX_GHOST;

public class Map {
    public static final int X_ARRAY = 21;
    public static final int Y_ARRAY = 23;

    @Expose
    private CellType[][] cellTypes = new CellType[X_ARRAY][Y_ARRAY];
    private final Pacman pacman = new Pacman();
    private final ArrayList<Ghost> ghosts = new ArrayList<>();

    private int numberOfDots = 0;
    private int numberOfLives = SettingController.getNumberOfLivesOfPacman();
    private int score = 0;
    private int numberOfEatenGhosts;
    private boolean ghostMode = false;
    private Timer ghostModeTimer;


    public static boolean isDirectionsEqualToPoint(double x, double y,Point2D point) {
        return point.getX() == x && point.getY() == y;
    }

    public static boolean isLocationValid(Point2D location) {
        int x = (int) location.getX();
        int y = (int) location.getY();
        return x >= 0 && x < X_ARRAY && y >= 0 && y <Y_ARRAY;
    }


    public Map() {
        pacman.setLocation((double) (X_ARRAY - 1)/2,(double) (Y_ARRAY - 1)/2);
        resetGhosts();
        initializeTable();
    }

    public Map(CellType[][] cellTypes) {
        pacman.setLocation((double) (X_ARRAY - 1)/2,(double) (Y_ARRAY - 1)/2);
        resetGhosts();
        this.cellTypes = cellTypes;
    }

    public int getNumberOfDots() {
        return numberOfDots;
    }

    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    public Ghost getGhost(int index) {
        if (index >= ghosts.size() || index < 0) return null;
        return ghosts.get(index);
    }

    public Ghost getGhost(Point2D location) {
        for (Ghost ghost : ghosts) {
            if (ghost.getLocation().equals(location)) return ghost;
        }
        return null;
    }

    public Pacman getPacman() {
        return pacman;
    }


    public CellType getCellType(int x,int y) {
        return cellTypes[x][y];
    }

    public CellType getCellType(Point2D point) {
        if (isLocationValid(point)) return getCellType((int) point.getX(), (int) point.getY());
        else return WALL;
    }

    public CellType[][] getCellTypes() {
        return cellTypes;
    }

    public Ghost getGhost(int x, int y) {
        for (int i = 0; i < MAX_GHOST; i++) {
            if (ghosts.get(i).getLocation().equals(new Point2D(x,y))) return ghosts.get(i);
        }
        return null;
    }

    public int getScore() {
        return score;
    }

    public int getNumberOfEatenGhosts() {
        return numberOfEatenGhosts;
    }

    public int getNumberOfLives() {
        return numberOfLives;
    }

    public int getLives() {
        return numberOfLives;
    }

    public boolean isGhostModeOn() {
        return ghostMode;
    }


    public void addGhost(Ghost ghost) {
        if (ghostMode) ghost.makeGhostBlue();
        else ghost.setGhostImage();
        ghosts.add(ghost);
    }

    public void increaseScore(int amount) {
        score+=amount;
    }

    public void reduceDots() {
        numberOfDots--;
    }

    public void reduceLives() {
        numberOfLives--;
    }



    public void activeGhostMode() {
        numberOfEatenGhosts = 0;
        ghostMode = true;
        for (Ghost ghost : ghosts) {
            ghost.makeGhostBlue();
        }
        if (ghostModeTimer != null) ghostModeTimer.cancel();
        ghostModeTimer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                deactivateGhostMode();
            }
        };
        ghostModeTimer.schedule(task,10000);
    }

    public void deactivateGhostMode() {
        ghostModeTimer = null;
        numberOfEatenGhosts = 0;
        ghostMode = false;
        for (Ghost ghost : ghosts) {
            ghost.setGhostImage();
        }
    }


    public void removeGhost(Ghost ghost) {
        numberOfEatenGhosts++;
        if (ghosts.contains(ghost)) {
            int x = (int) ghost.getLocation().getX();
            int y = (int) ghost.getLocation().getY();
            cellTypes[x][y] = EMPTY;
            ghosts.remove(ghost);
        }
    }

    public ArrayList<Integer> ghostsWeDontHave() {
        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= MAX_GHOST; i++) {
            numbers.add(i);
            for (Ghost ghost : ghosts) if (ghost.getGhostNumber() == i) numbers.remove(i);
        }
        return numbers;
    }

    public void resetGhosts() {
        for (Ghost ghost : ghosts) {
            int x = (int) ghost.getLocation().getX();
            int y = (int) ghost.getLocation().getY();

            ghosts.remove(ghost);
        }
        for (int i = 1; i <= MAX_GHOST; i++) {
            ghosts.add(new Ghost(i));
        }

        ghosts.get(0).setLocation(1,1);
        ghosts.get(1).setLocation(X_ARRAY - 2, 1);
        ghosts.get(2).setLocation(1,Y_ARRAY - 2);
        ghosts.get(3).setLocation(X_ARRAY - 2,Y_ARRAY - 2);
        if (MAX_GHOST >= 5) ghosts.get(4).setLocation(1,(double) (Y_ARRAY - 1)/2);
        if (MAX_GHOST >= 6) ghosts.get(5).setLocation((double) (X_ARRAY - 1)/2,1);
    }


    public void changeCellType(Point2D location, CellType type) {
        if (isLocationValid(location)) {
            cellTypes[(int) location.getX()][(int) location.getY()] = type;
        }
    }

    private void initializeTable() {
        numberOfDots = 0;
        CellType[][] tableType = MazeCreator.creatValidMaze();
        for (int i = 0; i < X_ARRAY; i++) {
            for (int j = 0; j < Y_ARRAY; j++) {
                this.cellTypes[i][j] = tableType[j][i];
                if (tableType[j][i] == DOT) numberOfDots++;

            }
        }
    }
    @Override
    public Map clone() {
        Map map = new Map();
        for (int i = 0; i < X_ARRAY; i++) {
            for (int j = 0; j < Y_ARRAY; j++) {
                map.cellTypes[i][j] = this.cellTypes[i][j];
            }
        }
        return map;
    }
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o instanceof Map) {
            Map map = (Map) o;
            for (int i = 0; i < Map.X_ARRAY; i++) {
                for (int j = 0; j < Map.Y_ARRAY; j++) {
                    if (map.cellTypes[i][j] != this.cellTypes[i][j]) return false;
                }
            }
            return true;
        } return false;
    }




    public boolean isThereGhost(Point2D location) {
        int x = (int) location.getX();
        int y = (int) location.getY();
        for (Ghost ghost : ghosts) {
            if (isDirectionsEqualToPoint(x,y,ghost.getLocation())) return true;
        }
        return false;
    }
}
