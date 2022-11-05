package program.view.graphic;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import program.controller.GameController;
import program.controller.SettingController;

import java.net.URL;
import java.util.ResourceBundle;

public class GameSettingController implements Initializable {
    @FXML
    private CheckBox hardModeCheckBox;
    @FXML
    private Button addLiveButton;
    @FXML
    private Label numberOfLives;
    @FXML
    private Button decreaseLiveButton;
    @FXML
    private Slider volumeSlider;
    @FXML
    private CheckBox muteButton;

    public void mute(ActionEvent actionEvent) {
        if (muteButton.isSelected()) SettingController.mute();
        else SettingController.unMute();
    }

    public void decreaseLive(ActionEvent actionEvent) {
        int lives = SettingController.getNumberOfLivesOfPacman();
        if(lives == 0) return;
        SettingController.setNumberOfLivesOfPacman(lives - 1);
        numberOfLives.setText(Integer.toString(lives - 1));
    }

    public void increaseLive(ActionEvent actionEvent) {
        int lives = SettingController.getNumberOfLivesOfPacman();
        if(lives == 5) return;
        SettingController.setNumberOfLivesOfPacman(lives + 1);
        numberOfLives.setText(Integer.toString(lives + 1));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                SettingController.setVolume(volumeSlider.getValue() / 100);
            }
        });
        volumeSlider.setValue(SettingController.getVolume() * 100);
        muteButton.setSelected(SettingController.isMute());
        numberOfLives.setText(Integer.toString(SettingController.getNumberOfLivesOfPacman()));
        hardModeCheckBox.setSelected(GameController.hardMode);
    }

    public void changeMode(ActionEvent actionEvent) {
        if (hardModeCheckBox.isSelected()) GameController.turnOnHardMode();
        else GameController.turnOffHardMode();
    }
}
