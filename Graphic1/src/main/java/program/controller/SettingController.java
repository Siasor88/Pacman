package program.controller;

import program.model.User;
import program.view.responses.ProfileMenuResponses;

import static program.view.graphic.WelcomeController.getRoot;
import static program.view.graphic.WelcomeController.goToLoginMenu;

public class SettingController {
    private static int numberOfLivesOfPacman = 3;
    private static double volume = 0.5;
    private static boolean isMute = false;

    public static double getVolume() {
        return volume;
    }

    public static int getNumberOfLivesOfPacman() {
        return numberOfLivesOfPacman;
    }

    public static ProfileMenuResponses changePassword(String oldPassword, String newPassword) {
        User user = LoginController.getCurrentUser();
        if (user.getPassword().equals(oldPassword)) {
            if (!oldPassword.equals(newPassword)) {
                user.changePassword(newPassword);
                ReadAndWriteDatabase.updateUser(user);
                return ProfileMenuResponses.PASSWORD_CHANGED_SUCCESSFULLY;
            } else return ProfileMenuResponses.PLEASE_ENTER_A_NEW_PASSWORD;
        } else return ProfileMenuResponses.CURRENT_PASSWORD_IS_INVALID;
    }

    public static ProfileMenuResponses changeNickname(String nickname, String password) {
        User user = LoginController.getCurrentUser();
        if (user.getPassword().equals(password)) {
            if (!LoginController.doesNicknameExists(nickname)) {
                user.changeNickname(nickname);
                ReadAndWriteDatabase.updateUser(user);
                return ProfileMenuResponses.NICKNAME_CHANGED_SUCCESSFULLY;
            } else return ProfileMenuResponses.USER_WITH_NICKNAME_ALREADY_EXISTS;
        } else return ProfileMenuResponses.INVALID_PASSWORD;
    }

    public static void setVolume(double volume) {
        SettingController.volume = volume;
    }

    public static void setNumberOfLivesOfPacman(int livesOfPacman) {
        if (livesOfPacman <= 5 && livesOfPacman >= 0) SettingController.numberOfLivesOfPacman = livesOfPacman;
    }

    public static boolean isMute() {
        return isMute;
    }

    public static void mute() {
        SettingController.isMute = true;
    }
    public static void unMute() {
        SettingController.isMute = false;
    }

    public static void deleteUser(User currentUser) {
        ReadAndWriteDatabase.deleteUser(currentUser);
    }
}
