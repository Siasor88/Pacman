package program.model;

import com.google.gson.annotations.Expose;
import program.model.game.Map;

import java.util.ArrayList;

public class User {
    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private String nickname;
    @Expose
    private int score;
    @Expose
    private ArrayList<Map> maps;

    public User(String username, String password, String nickname, ArrayList<Map> maps)  {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        if (maps != null) this.maps = maps;
        else this.maps = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaps(ArrayList<Map> maps) {
        this.maps = maps;
    }

    public void changePassword(String newPassword) {
        password = newPassword;
    }

    public void changeNickname(String newNickname) {
        nickname = newNickname;
    }

    public void addMap(Map map) {
        if (!maps.contains(map)) maps.add(map);
    }
    public void removeMap(Map map) {
        maps.remove(map);
        for(Map map1 : maps) {
           if (map1.equals(map)) removeMap(map1);
        }
    }

    @Override
    public String toString(){
        return "Username: " + username + " Nickname: " + nickname + " Score: " + score ;
    }


}
