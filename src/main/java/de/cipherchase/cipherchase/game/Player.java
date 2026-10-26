package de.cipherchase.cipherchase.game;

//import de.cipherchase.cipherchase.persistence.User; nicht benutzt? 

/**
 * @version 1.0 - 2026-10-25
 * @author Manuel
 * Represents a single player in a Gamesession
 */
public class Player {
    
    /**Containts the Role of the player, wether thief or officer */
    private final Role role;
    
    /**contains the unique player ID, same as in User.java */
    private final long playerID;

    /**Contains the room the player is in (one to seven). Seven represents the freedom*/
    private int roomPosition;

    /**
     * The two Roles a player can be, thief or officer
     */
    enum Role {
        THIEF,
        OFFICER
    }

    /**
     * Constructor to create a player, depending of the role the roomPosition is defined
     * @param role The Role the Player is playing
     * @param playerID The unique ID of each player, drawn from User.java
     */
    public Player(Role role, long playerID) {
        this.role = role;
        this.playerID = playerID;
        if (this.role == Role.OFFICER) {
            roomPosition = 1;
        } else {
            roomPosition = 3;
        }
    }

    /**
     * Gives the room number, the player is currently in
     * @return Returns the room number as int
     */
    public int getRoomPosition() {return roomPosition;}

    /**
     * Gives the unique ID of each player
     * @return Returns the ID as Reference to User.java ?????
     */
    public long getPlayerID() {return playerID;}

    /**
     * Gives the role the player is playing
     * @return Returns the role as the Enum Role
     */
    public Role getRole() {return role;}


    /**
     * Increments the current room position of the player
     */
    public void moveToNextRoom() {
        roomPosition += 1;
    }

}
