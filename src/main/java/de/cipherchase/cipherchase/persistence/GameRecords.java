package de.cipherchase.cipherchase.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;



/**
 * @version 1.0 - 2026-10-25
 * @author Manuel
 * 
 * 
*/
@Entity(name = "GameRecords")
public class GameRecords {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long matchID;

    @Column (name = "gameTimestamp", updatable = false)
    private LocalDateTime gameTimestamp;

    @ManyToOne 
    @JoinColumn (name = "winner", referencedColumnName = "userID")
    private User winner;

    @ManyToOne 
    @JoinColumn (name = "looser", referencedColumnName = "userID")
    private User looser;


    protected GameRecords(){};      //wird benötigt, da sonst eine Fehlermeldung geworfen wird


    public GameRecords (User winner, User looser) {
        gameTimestamp = LocalDateTime.now();
        this.winner = winner;
        this.looser = looser;
    }

}
