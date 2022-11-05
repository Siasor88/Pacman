package program.model.game;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import program.Main;
import program.model.enums.Directions;
import program.model.exeptions.InvalidDirections;
import program.model.game.MovingObjects;

import java.net.URL;

public class Pacman extends MovingObjects {


    public Pacman() {
        setLocation(new Point2D(11, 13));
        URL url = Main.class.getResource("pictures/game/pacmanRight.gif");

        setImage(new Image(url.toExternalForm()));
    }

    public void setImage(Directions direction) {
        String name = direction.toString().toLowerCase().replace("r", "R").replace("l", "L").replace("d", "D").replace("u", "U");
        setImage(new Image(Main.class.getResource("pictures/game/pacman" + name + ".gif").toExternalForm()));
    }

    @Override
    public void move(Point2D velocity) {
        try {
            super.move(velocity);
            setImage(currentDirection);
        } catch (InvalidDirections ignored) {
        }
    }

    @Override
    public void setCurrentDirection(Directions direction) {
        currentDirection = direction;
        setImage(currentDirection);
    }

}
