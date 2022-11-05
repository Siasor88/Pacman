package program.view.graphic;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import program.Main;
import program.controller.GameController;
import program.controller.LoginController;
import program.controller.ReadAndWriteDatabase;
import program.controller.SettingController;
import program.model.User;
import program.model.enums.Directions;
import program.model.game.Game;
import program.model.game.Map;
import program.model.game.Pacman;

import static program.controller.GameController.hardMode;


public class GameGraphicalController implements Initializable, EventHandler<KeyEvent> {

    private static Game currentGame = null;
    @FXML
    private Label status;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label highScoreLabel;
    @FXML
    private ImageView live1;
    @FXML
    private ImageView live2;
    @FXML
    private ImageView live3;
    @FXML
    private ImageView live4;
    @FXML
    private ImageView live5;
    @FXML
    private BorderPane gamePane;

    final static Media wanka = new Media(new File("src/main/resources/program/music/wanka.wav").toURI().toString());
    final static Media start = new Media(new File("src/main/resources/program/music/start.wav").toURI().toString());
    final static Media death = new Media(new File("src/main/resources/program/music/death.wav").toURI().toString());
    final static Media eatGhost = new Media(new File("src/main/resources/program/music/death.wav").toURI().toString());

    private final Image heart = new Image(Main.class.getResource("pictures/game/hearts.png").toExternalForm());
    private int highScore;
    private Timeline timeline = new Timeline();

    int counter = 0;

    public static void eatGhosts() {
        play(new MediaPlayer(eatGhost));
    }

    public static void wankaWanka() {
        MediaPlayer player = new MediaPlayer(wanka);
        play(player);
    }

    private static void play(MediaPlayer player) {
        if (!SettingController.isMute()) {
            double volume = SettingController.getVolume();
            player.setVolume(volume);
            player.seek(Duration.ZERO);
            player.setCycleCount(1);
            player.play();
        }
    }

    public static void startMusic() {
        MediaPlayer player = new MediaPlayer(start);
        play(player);
    }

    public static void death() {
        play(new MediaPlayer(death));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (currentGame == null) {
            createNewGame();
        }
        counter = 0;
        gamePane.setCenter(currentGame);
        timeline = new Timeline();
        setTimeline();
        User user = LoginController.getCurrentUser();
        if (user != null) highScore = user.getScore();
        else highScore = 0;
        setScore(highScore);
        setLivesPictures(currentGame.getMap().getNumberOfLives());
        startMusic();
    }


    @Override
    public void handle(KeyEvent keyEvent) {
       if (currentGame != null) {
           Pacman pacman = currentGame.getMap().getPacman();
           switch (keyEvent.getCode()) {
               case DOWN:
                   pacman.setCurrentDirection(Directions.DOWN);
                   break;
               case UP:
                   pacman.setCurrentDirection(Directions.UP);
                   break;
               case LEFT:
                   pacman.setCurrentDirection(Directions.LEFT);
                   break;
               case RIGHT:
                   pacman.setCurrentDirection(Directions.RIGHT);
                   break;
               case SPACE:
                   if (timeline.getStatus().equals(Animation.Status.PAUSED)) timeline.play();
                   else timeline.pause();
           }
       }
    }

    @FXML
    private void pause() {
        timeline.pause();
    }

    @FXML
    private void continueGame() {
       if (currentGame != null)timeline.play();
    }


    @FXML
    private void startNewGame() {
        timeline = new Timeline();
        createNewGame();
        startMusic();
        timeline.play();
    }

    private void createNewGame() {
        Map map = new Map();
        currentGame = new Game(map);
        currentGame.update();
        gamePane.setCenter(currentGame);
        timeline = new Timeline();
        setTimeline();
    }

    private void setTimeline() {
        KeyFrame pacmanFrame = new KeyFrame(Duration.millis(150), (ActionEvent actionEvent) -> {
            updateFrames();
        });
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.getKeyFrames().add(pacmanFrame);
    }

    private void updateFrames() {
        if (counter % 2 == 0) {
            Map map = currentGame.getMap();
            Pacman pacman = map.getPacman();
            GameController.movePacman(map, pacman.getCurrentDirection());
            currentGame.update();
            int score = map.getScore();
            int lives = map.getLives();
            int numberOfDots = currentGame.getNumberOfDots();
            if (numberOfDots == 0) {
                gameOver();
                timeline.stop();
            } else if (lives == 0) {
                setLivesPictures(0);
                gameOver();
                timeline.stop();
            }
            setLivesPictures(lives);
            setScore(score);
            if (hardMode || counter % 7 != 0) updateGhostFrames();
            if (counter == 100) counter = 0;
        }
        counter++;
    }

    private void updateGhostFrames() {
        Map map = currentGame.getMap();
        GameController.moveGhosts(map);
        currentGame.update();
        if (map.getNumberOfLives() == 0) {
            setLivesPictures(0);
            gameOver();
            timeline.stop();
        }
    }

    private void setScore(int score) {
        scoreLabel.setText(Integer.toString(score));
        if (score > highScore) highScore = score;
        highScoreLabel.setText(Integer.toString(highScore));
    }

    private void setLivesPictures(int lives) {

        switch (lives) {
            case 0:
                live1.setImage(null);
                live2.setImage(null);
                live3.setImage(null);
                live4.setImage(null);
                live5.setImage(null);
                break;
            case 1:
                live1.setImage(heart);
                live2.setImage(null);
                live3.setImage(null);
                live4.setImage(null);
                live5.setImage(null);
                break;
            case 2:
                live1.setImage(heart);
                live2.setImage(heart);
                live3.setImage(null);
                live4.setImage(null);
                live5.setImage(null);
                break;
            case 3:
                live1.setImage(heart);
                live2.setImage(heart);
                live3.setImage(heart);
                live4.setImage(null);
                live5.setImage(null);
                break;
            case 4:
                live1.setImage(heart);
                live2.setImage(heart);
                live3.setImage(heart);
                live4.setImage(heart);
                live5.setImage(null);
                break;
            case 5:
                live1.setImage(heart);
                live2.setImage(heart);
                live3.setImage(heart);
                live4.setImage(heart);
                live5.setImage(heart);
                break;
        }

    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static void setCurrentGame(Game currentGame) {
        GameGraphicalController.currentGame = currentGame;
    }

    public void quit(ActionEvent actionEvent) {
        pause();
        updateUser();
        WelcomeController.goToMainMenu();
    }

    private void updateUser() {
        User user = LoginController.getCurrentUser();
        user.setScore(highScore);
        ReadAndWriteDatabase.updateUser(user);
    }

    public void close(ActionEvent actionEvent) {
        quit(null);
        System.exit(0);
    }

    public void gameOver() {
        currentGame = null;
        status.setTextFill(Color.BLACK);
        status.setBackground(new Background(new BackgroundFill(Color.RED,null,null)));
        status.setText("GAME OVER!");
        updateUser();
    }
}
