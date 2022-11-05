package program.controller;

import program.model.User;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreBoardController {
    public static ArrayList<User> getSortedUsers() {
        ArrayList<User> users = ReadAndWriteDatabase.getAllUsers();
        int numberOfUsers = users.size();
        for (int i = 0; i < numberOfUsers - 1; i++) {
            for (int j = 0; j < numberOfUsers - i - 1; j++) {
                if (users.get(j).getScore() > users.get(j + 1).getScore()) {
                    Collections.swap(users, j, j + 1);
                }
                if (users.get(j).getScore() == users.get(j + 1).getScore()) {
                    if (users.get(j).getNickname().compareToIgnoreCase(users.get(j + 1).getNickname()) > 0) {
                        Collections.swap(users, j, j + 1);
                    }
                }
            }
        }

        return users;
    }
}
