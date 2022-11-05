package program.controller;

import javafx.geometry.Point2D;
import program.model.enums.CellType;
import program.model.enums.Directions;
import program.model.game.Ghost;
import program.model.game.Map;
import program.view.graphic.GameGraphicalController;
import program.view.responses.GameResults;

import java.util.*;

import static program.model.enums.CellType.*;
import static program.model.game.Map.isLocationValid;
import static program.model.game.MovingObjects.getDirectionByVelocity;
import static program.view.responses.GameResults.*;

public class GameController {
    public static boolean hardMode = true;
    public static void movePacman(Map map, Directions direction) {
        Point2D pacmanLocation = new Point2D(0, 0).add(map.getPacman().getLocation());
        Point2D velocity = getVelocityByDirection(direction);
        Point2D pacmanPossibleLocation = pacmanLocation.add(velocity);
        if (pacmanLocation.equals(pacmanPossibleLocation)) return;
        CellType pacmanPossibleLocationCellType = map.getCellType(pacmanPossibleLocation);

        if (map.isThereGhost(pacmanPossibleLocation)) {
            if (map.isGhostModeOn()) {
                Ghost ghost = map.getGhost(pacmanPossibleLocation);
                if (ghost != null) {
                    GameGraphicalController.eatGhosts();
                    eatGhost(map,ghost);
                    movePacman(map, velocity);
                    return;
                }
            } else reduceLive(map);
        }
        if (pacmanPossibleLocationCellType == WALL) {
            map.getPacman().setCurrentDirection(getDirectionByVelocity(velocity));
            return;
        }

        map.changeCellType(pacmanLocation, EMPTY);
        map.changeCellType(pacmanPossibleLocation, PACMAN);
        movePacman(map, velocity);

        if (pacmanPossibleLocationCellType == DOT) {
            GameGraphicalController.wankaWanka();
            map.increaseScore(5);
            map.reduceDots();
            return;
        }

        if (pacmanPossibleLocationCellType == ENERGY_BOMB) {
            GameGraphicalController.wankaWanka();
            map.increaseScore(50);
            map.activeGhostMode();
        }

    }

    private static void eatGhost(Map map, Ghost ghost) {
        map.removeGhost(ghost);
        addGhostToGame(map, ghost);
        map.increaseScore(map.getNumberOfEatenGhosts() * 200);
    }

    private static void reduceLive(Map map) {
        ArrayList<Ghost> ghostss = (ArrayList<Ghost>) map.getGhosts().clone();
        map.reduceLives();
        GameGraphicalController.death();
        map.getGhosts().removeAll(map.getGhosts());
        Timer addAllGhostsTimer = new Timer();
        TimerTask task = new TimerTask() {
            ArrayList<Ghost> ghosts = ghostss;
            @Override
            public void run() {
                if (map.getLives() == 0) return;
                for (int i = 0; i < ghosts.size(); i++) {
                    Ghost ghost1 = new Ghost(ghosts.get(i).getGhostNumber());
                    map.addGhost(ghost1);
                    if (map.getPacman().getLocation().equals(ghost1.getLocation())){
                        if (map.isGhostModeOn()) {
                            eatGhost(map,ghost1);
                            ghosts.remove(ghost1);
                            --i;
                        } else {
                            reduceLive(map);
                            return;
                        }
                    }
                }
            }
        };
        addAllGhostsTimer.schedule(task, 2000);
    }

    public static void addGhostToGame(Map map, Ghost ghost) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                synchronized (map) {
                    Ghost ghost1 = new Ghost(ghost.getGhostNumber());
                    map.addGhost(ghost1);
                    if (map.getPacman().getLocation().equals(ghost1.getLocation())) {
                        if (map.isGhostModeOn()) eatGhost(map,ghost);
                        else reduceLive(map);
                    }
                }
            }
        };
        timer.schedule(task, 5000);
    }

    public static void moveGhosts(Map map) {
        ArrayList<Ghost> ghosts = map.getGhosts();
        for (int i = 0; i < ghosts.size(); i++) {
            moveGhost(map, ghosts.get(i));
        }
    }

    private static void moveGhost(Map map, Ghost ghost) {
        Directions rightDirection = getTheCorrectDirection(map, ghost);
        Point2D location = ghost.getLocation().add(getVelocityByDirection(rightDirection));
        if (map.getPacman().getLocation().equals(location) && !map.isGhostModeOn()) reduceLive(map);
        else if (!map.getPacman().getLocation().equals(location)) {
            ghost.setLocation(location);
        }
    }

    private static Directions getTheCorrectDirection(Map map, Ghost ghost) {
        Point2D ghostLocation = new Point2D(0, 0).add(ghost.getLocation());
        ArrayList<Directions> possibleDirections = getPossibleDirections(map, ghostLocation);
        possibleDirections.remove(Directions.NONE);
        if (possibleDirections.size() == 0) return Directions.NONE;
        Directions direction = wherePacmanIsAccordingToLocation(map, ghostLocation);
        if (possibleDirections.contains(direction) && !map.isGhostModeOn()) return direction;
        else {
            if (map.isGhostModeOn()) possibleDirections.remove(direction);
            if(possibleDirections.size() == 0) return Directions.NONE;
            int random = Math.abs(new Random(System.currentTimeMillis()).nextInt() % possibleDirections.size());
            return possibleDirections.get(random);
        }
    }

    private static Directions wherePacmanIsAccordingToLocation(Map map, Point2D location) {
        int xLoc = (int) location.getX();
        int xPacman = (int) map.getPacman().getLocation().getX();
        int yLoc = (int) location.getY();
        int yPacman = (int) map.getPacman().getLocation().getY();
        if (xLoc != xPacman && yLoc != yPacman) return Directions.NONE;
        else if (xLoc != xPacman) {
            if (xLoc > xPacman) return Directions.LEFT;
            else return Directions.RIGHT;
        } else if (yLoc != yPacman) {
            if (yLoc > yPacman) return Directions.UP;
            else return Directions.DOWN;
        } else return Directions.HERE;
    }

    private static ArrayList<Directions> getPossibleDirections(Map map, Point2D location) {
        ArrayList<Directions> result = new ArrayList<>();
        Point2D[] possibleLoc = new Point2D[4];
        possibleLoc[0] = location.add(1, 0);
        possibleLoc[1] = location.add(-1, 0);
        possibleLoc[2] = location.add(0, 1);
        possibleLoc[3] = location.add(0, -1);
        for (Point2D loc : possibleLoc) {
            if (!map.getCellType(loc).equals(WALL) && map.getGhost(loc) == null)
                result.add(getDirectionByVelocity(loc.subtract(location)));
        }
        while (result.contains(Directions.NONE)) result.remove(Directions.NONE);
        result.add(Directions.NONE);
        return result;
    }

    private static void movePacman(Map map, Point2D velocity) {
        Point2D previousLocationOfPacman = map.getPacman().getLocation();
        if (isLocationValid(previousLocationOfPacman.add(velocity))) {
            map.getPacman().move(velocity);
            map.changeCellType(previousLocationOfPacman, EMPTY);
            map.changeCellType(map.getPacman().getLocation(), PACMAN);
        }
    }

    public static Point2D getVelocityByDirection(Directions direction) {
        switch (direction) {
            case DOWN:
                return new Point2D(0, 1);
            case UP:
                return new Point2D(0, -1);
            case RIGHT:
                return new Point2D(1, 0);
            case LEFT:
                return new Point2D(-1, 0);
            default:
                return new Point2D(0, 0);
        }
    }

    public static void turnOnHardMode() {
        hardMode = true;
        Ghost.setMaxGhost(6);
    }
    public static void turnOffHardMode() {
        hardMode = false;
        Ghost.setMaxGhost(4);
    }


}
