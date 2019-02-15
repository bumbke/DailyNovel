package main.dailynovel.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
    private String userMail;
    public String getUserMail() {
        return userMail;
    }
    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    private String userName;
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    private ArrayList<String> novelSubscribe;
    public ArrayList<String> getNovelSubscribe() {
        return novelSubscribe;
    }
    public void setNovelSubscribe(ArrayList<String> novelSubscribe) {
        this.novelSubscribe = novelSubscribe;
    }

    private ArrayList<String> novelLiked;
    public ArrayList<String> getNovelLiked() {
        return novelLiked;
    }
    public void setNovelLiked(ArrayList<String> novelLiked) {
        this.novelLiked = novelLiked;
    }

    public User() {

    }

    public User(String userMail, String userName, ArrayList<String> novelSubscribe, ArrayList<String> novelLiked) {
        this.userMail = userMail;
        this.userName = userName;
        this.novelSubscribe = novelSubscribe;
        this.novelLiked = novelLiked;
    }
}
