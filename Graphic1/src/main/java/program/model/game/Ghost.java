package program.model.game;

import javafx.scene.image.Image;
import program.Main;
import program.model.exeptions.InvalidGhostNumber;

import javafx.geometry.Point2D;

import static program.model.game.Map.X_ARRAY;
import static program.model.game.Map.Y_ARRAY;

public class Ghost extends MovingObjects {
    public static int MAX_GHOST = 6;
    private int ghostNumber;

    public Ghost(int ghostNumber) {
        if (ghostNumber > MAX_GHOST || ghostNumber < 1) throw new InvalidGhostNumber();
        this.ghostNumber = ghostNumber;
        setGhostImage();
        location = new Point2D(0, 0);
        switch (ghostNumber) {
            case 1: setLocation(1,1); break;
            case 2: setLocation(X_ARRAY - 2, 1); break;
            case 3: setLocation(1,Y_ARRAY - 2); break;
            case 4: setLocation(X_ARRAY - 2,Y_ARRAY - 2); break;
            case 5: setLocation(1,(double) (Y_ARRAY - 1)/2); break;
            case 6: setLocation((double) (X_ARRAY - 1)/2,1); break;
        }

    }

    public int getGhostNumber() {
        return ghostNumber;
    }

    public void setGhostImage() {
        image = new Image(Main.class.getResource("pictures/game/ghost" + ghostNumber + ".gif").toExternalForm());
    }

    public void makeGhostBlue() {
        image = new Image(Main.class.getResource("pictures/game/blueghost.gif").toExternalForm());
    }

    @Override
    public void move(Point2D velocity) {
        super.move(velocity);
    }


    public static void setMaxGhost(int maxGhost) {
        MAX_GHOST = maxGhost;
    }
}
