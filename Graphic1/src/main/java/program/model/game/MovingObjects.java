package program.model.game;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import program.model.enums.Directions;
import program.model.exeptions.InvalidDirections;

public class MovingObjects {
    protected Point2D location;
    protected Image image;
    protected Directions currentDirection;

    public static boolean isVelocityValid(Point2D velocity) {
        double xRay = Math.abs(velocity.getX());
        double yRay = Math.abs(velocity.getY());
        return isVelocityValid(xRay, yRay);
    }

    public static boolean isVelocityValid(double xRay, double yRay) {
        return (xRay == 1 && yRay == 0) || (xRay == 0 && yRay == 1);
    }

    public static Directions getDirectionByVelocity(Point2D velocity) {
        if (isVelocityValid(velocity)) {
            if (velocity.equals(new Point2D(1, 0))) return Directions.RIGHT;
            else if (velocity.equals(new Point2D(-1, 0))) return Directions.LEFT;
            else if (velocity.equals(new Point2D(0, -1))) return Directions.UP;
            else if (velocity.equals(new Point2D(0, 1))) return Directions.DOWN;
            else return Directions.NONE;
        } else return Directions.NONE;
    }

    public MovingObjects() {
        currentDirection = Directions.NONE;
    }

    public Directions getCurrentDirection() {
        return currentDirection;
    }

    public Image getImage() {
        return image;
    }

    public Point2D getLocation() {
        return location;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setLocation(Point2D location) {
        this.location = location;
    }

    public void setLocation(double x, double y) {
        setLocation(new Point2D(x,y));
    }

    public void setCurrentDirection(Directions currentDirection) {
        this.currentDirection = currentDirection;
    }

    public void move(Point2D velocity) {
        if (isVelocityValid(velocity)) {
            location = location.add(velocity);
            currentDirection = getDirectionByVelocity(velocity);
        } else throw new InvalidDirections();
    }


}
