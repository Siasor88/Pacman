package program.controller;

import program.model.User;
import program.view.responses.LoginMenuResponses;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LoginController {

    static User currentUser;
    static boolean isGuest = false;

    public static boolean doesUsernameExists(String username) {
        try {
            FileReader fileReader = new FileReader(ReadAndWriteDatabase.getUserFileByUserAddr(username + ".json"));
            fileReader.close();
            return true;
        } catch (IOException e) {
            return false;
        }

    }

    public static boolean doesNicknameExists(String nickname) {
        ArrayList<User> users = ReadAndWriteDatabase.getAllUsers();
        for (User user : users) {
            if (user.getNickname().equals(nickname)) return true;
        }
        return false;
    }

    public static boolean isPasswordCorrect(String username, String password) {
        if (doesUsernameExists(username)) {
            User user = ReadAndWriteDatabase.getUser(username + ".json");
            return user.getPassword().equals(password);
        } else return false;
    }


    public static LoginMenuResponses createUser(String username, String nickname, String password) {
        if (doesUsernameExists(username)) return LoginMenuResponses.USER_WITH_THIS_USERNAME_EXITS;
        else if (doesNicknameExists(nickname)) return LoginMenuResponses.USER_WITH_THIS_NICKNAME_EXITS;
        else {
            User user = new User(username, password, nickname, null);
            ReadAndWriteDatabase.writeUserToUsersDirectory(user);
            return LoginMenuResponses.USER_CREATED_SUCCESSFULLY;
        }
    }

    public static LoginMenuResponses login(String username, String password) {
        if (doesUsernameExists(username)) {
            if (isPasswordCorrect(username, password)) {
                setCurrentUser(username);
                return LoginMenuResponses.USER_LOGIN_SUCCESSFUL;
            } else return LoginMenuResponses.PASSWORD_AND_USERNAME_DIDNT_MATCH;
        } else return LoginMenuResponses.PASSWORD_AND_USERNAME_DIDNT_MATCH;
    }


    public static void logout() {
        isGuest = false;
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String username) {
        currentUser = ReadAndWriteDatabase.getUser(username + ".json");
    }

    public static void setCurrentUser(User user) {
        LoginController.currentUser = user;
    }

    public static boolean isGuest() {
        return isGuest;
    }

    public static void setIsGuest(boolean isGuest) {
        LoginController.isGuest = isGuest;
    }
}


