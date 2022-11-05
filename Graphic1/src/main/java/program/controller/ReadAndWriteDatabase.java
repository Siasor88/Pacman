package program.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import program.model.User;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReadAndWriteDatabase {
    public static final String usersAddr = "src/main/resources/Users/";

    public static User getUser(String usersAddr) {
        File file = getUserFileByUserAddr(usersAddr);
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        User user;
        try {
            user = gson.fromJson(new FileReader(file), User.class);
        } catch (Exception e) {
            return null;
        }
        return user;
    }

    public static void writeUserToUsersDirectory(User user) {
        if (LoginController.isGuest()) return;
        try {
            FileWriter fileWriter = new FileWriter(usersAddr + user.getUsername() + ".json");
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            gson.toJson(user, fileWriter);
            fileWriter.close();
        } catch (IOException ignored) {

        }
    }

    public static File getUserFileByUserAddr(String userAddr) {
        return new File(usersAddr + userAddr);
    }

    public static ArrayList<User> getAllUsers() {
        String[] userAddrs;
        ArrayList<User> users = new ArrayList<>();
        File usersDirectory = new File(usersAddr);
        userAddrs = usersDirectory.list();
        if (userAddrs == null) return users;
        for (String userAddr : userAddrs) {
            User user = ReadAndWriteDatabase.getUser(userAddr);
            if (user != null) users.add(user);
        }
        return users;
    }

    public static void updateUser(User user) {
        ReadAndWriteDatabase.writeUserToUsersDirectory(user);
    }


    public static void deleteUser(User currentUser) {
        if (LoginController.isGuest()) return;
        File file = getUserFileByUserAddr(currentUser.getUsername() + ".json");
        file.delete();
    }
}
