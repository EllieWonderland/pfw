package de.cipherchase.cipherchase.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



/**
 * @version 1.0 - 2026-10-25
 * @author Manuel
 * 
 * 
*/
@Entity
@Table (name = "userTable")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userID;

    @Column(nullable = false, unique = true, length = 30)
    private String userName;

    @Column (nullable = false, unique = false, length = 30)
    private String userPassword;

    @Column (nullable = true, unique = false, length = 70)
    private String userAvatarURL;

    @Column (nullable = false, unique = false)
    private int playedGames = 0;

    @Column (nullable = false, unique = false)
    private int wonGames = 0;

    
    public User(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    //////////////////////////////////// GETTER and SETTER ////////////////////////////////////
    public long getUserID() {return userID;}
    public String getUserName() {return userName;};

    public void setUserAvatar(String userAvatarURL) {
        this.userAvatarURL = userAvatarURL;
    }
    public String getUserAvatar() {return userAvatarURL;};

    public int getPlayedGames() {return playedGames;};
    public void incrementPlayedGames() {playedGames++;};

    public int getWonGames() {return wonGames;};
    public void incrementWonGames() {wonGames++;};



}
