package program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import java.io.File;


public class Main extends Application{
    public static Stage stage;
    public static Image deaFaultBackgroundImage;
    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;
        GridPane root = FXMLLoader.load(getClass().getResource("Scenes/WelcomeMenu.fxml"));
        deaFaultBackgroundImage= new Image(getClass().getResource("pictures/backgroundWelcomeMenu.jpg").toExternalForm(),600,400,true,false);
        BackgroundImage backgroundImage = new BackgroundImage(deaFaultBackgroundImage, BackgroundRepeat.NO_REPEAT,null, BackgroundPosition.CENTER,BackgroundSize.DEFAULT);
        root.setBackground(new Background(backgroundImage));

//        FXMLLoader loader = new FXMLLoader(getClass().getResource("Scenes/Game.fxml"));
//        BorderPane root = loader.load();
//        Scene scene = new Scene(root);
//        scene.setOnKeyPressed(loader.getController());
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }




}
