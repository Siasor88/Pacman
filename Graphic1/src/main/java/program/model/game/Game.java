package program.model.game;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import program.Main;
import program.model.enums.CellType;
import program.model.game.Ghost;
import program.model.game.Map;
import program.model.game.MazeCreator;

import static program.model.enums.CellType.*;
import static program.model.game.Ghost.MAX_GHOST;

public class Game extends Pane {
    private static final int X_ARRAY = 21;
    private static final int Y_ARRAY = 23;
    public static final double CELL_WIDTH = 20.0;
    private Image wall;
    private Image energyBomb;
    private Image dot;
    private final ImageView[][] table = new ImageView[X_ARRAY][Y_ARRAY];
    private CellType[][] cellTypes;
    private Map map;
    private int numberOfDots;


    public Game(Map map) {
        super();
        this.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
        cellTypes = map.getCellTypes();
        this.map = map;
        initialize();
        numberOfDots = 0;
    }

    public Map getMap() {
        return map;
    }

    public void initialize() {
        wall = new Image(Main.class.getResource("pictures/game/wall.png").toExternalForm());
        energyBomb = new Image(Main.class.getResource("pictures/game/whitedot.png").toExternalForm());
        dot = new Image(Main.class.getResource("pictures/game/smalldot.png").toExternalForm());
        setTable();
    }

    public void setTable() {
        for (int row = 0; row < Y_ARRAY; row++) {
            for (int column = 0; column < X_ARRAY; column++) {
                ImageView imageView = getValidImageView(column, row);
                Image image = getImageByLocation(column, row);
                imageView.setImage(image);
                table[column][row] = imageView;
                this.getChildren().add(imageView);
            }
        }
        addGhosts();
    }

    private ImageView getValidImageView(double column, double row) {
        ImageView imageView = new ImageView();
        imageView.setFitHeight(CELL_WIDTH);
        imageView.setFitWidth(CELL_WIDTH);
        imageView.setX(column * CELL_WIDTH);
        imageView.setY(row * CELL_WIDTH);
        return imageView;
    }

    private void addGhosts() {
        for (Ghost ghost : map.getGhosts()) {
            addGhost(ghost);
        }
    }

    private void addGhost(Ghost ghost) {
        int x = (int) ghost.getLocation().getX();
        int y = (int) ghost.getLocation().getY();
        table[x][y].setImage(ghost.getImage());
    }


    public Image getImageByLocation(int xArray, int yArray) {
        CellType type = cellTypes[xArray][yArray];
        if (type == WALL) return wall;
        else if (type == DOT) {
            numberOfDots += 1;
            return dot;
        } else if (type == ENERGY_BOMB) return energyBomb;
        else if (type == PACMAN) return map.getPacman().getImage();
        else if (type == EMPTY) return null;
        else return null;
    }

    public Ghost getGhost(int x, int y) {
        for (int i = 0; i < MAX_GHOST; i++) {
            if (map.getGhosts().get(i).getLocation().equals(new Point2D(x, y))) return map.getGhosts().get(i);
        }
        return null;
    }

    public void update() {
        numberOfDots = 0;
        for (int i = 0; i < X_ARRAY; i++) {
            for (int j = 0; j < Y_ARRAY; j++) {
                table[i][j].setImage(getImageByLocation(i, j));
            }
        }
        addGhosts();
    }

    public int getNumberOfDots() {
        return numberOfDots;
    }
}
