package de.cipherchase.cipherchase.game;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.cipherchase.cipherchase.game.service.MatchmakingService;
import de.cipherchase.cipherchase.persistence.User;


/**
 * @version 1.0 - 2026-10-25
 * @author Manuel
 * Contains a complete Game between two Users
 */
public class GameSession {
 

    private Door[] doorArray = new Door[7];
    public Player thief;
    public Player officer;

    /*
    public User thiefUser;          ich glaube inzwischen überflüssig
    public User officerUser;
    */

    private boolean finished = false;
    private Player winner;
    private Player looser;

    private int time = 240000; //millisekunden


    private MatchmakingService matchmakingService;

    public GameSession(User user1, User user2, MatchmakingService matchmakingService) {
        createPlayers(user1, user2);
        createDoors();
        runTimer();
        this.matchmakingService = matchmakingService;
    }





    private void createDoors() {
        for(int i=0; i<7; i++) {
            doorArray[i] = new Door();
        }
    }

    private void createPlayers(User user1, User user2) {
        Random random = new Random();
        Player.Role role1;
        Player.Role role2;

        if (random.nextBoolean()) {
            role1 = Player.Role.THIEF;
        } else {
            role1 = Player.Role.OFFICER;
        }

        if (role1 == Player.Role.THIEF) {
            role2 = Player.Role.OFFICER;
        } else {
            role2 = Player.Role.THIEF;
        }

        if (role1 == Player.Role.THIEF) {
            thief = new Player(role1, user1.getUserID());
            officer = new Player(role2, user2.getUserID());
            //thiefUser = user1;
            //officerUser = user2;
        } else {
            thief = new Player(role2, user2.getUserID());
            officer = new Player(role1, user1.getUserID());
            //thiefUser = user2;
            //officerUser = user1;         
        }
    }

    public synchronized boolean[] submitGuess (Player player, int[] guess) {

        Door currentDoor = doorArray[player.getRoomPosition()-1];
        Attempt attempt = new Attempt(currentDoor, guess);
        if (attempt.isCorrect()) {
            player.moveToNextRoom();
        } else if (!attempt.isCorrect() && player.getRole() == Player.Role.THIEF) {
            currentDoor.failedCodeTraces.add(guess);
        }
        checkEndConditions();
        return attempt.getMisplacedMatches();
    }


    public synchronized void checkEndConditions() {
        if (thief.getRoomPosition() == 7) {
            this.finished = true;
            this.winner = thief;
            this.looser = officer;
            matchmakingService.notifyFinishedGame(this);
        } else if (time == 0) {
            this.finished = true;
            this.winner = officer;
            this.looser = thief;
            matchmakingService.notifyFinishedGame(this);
        }
        if (thief.getRoomPosition() == officer.getRoomPosition()) {
            finished = true;
            winner = officer;
            this.looser = thief;
            matchmakingService.notifyFinishedGame(this);
        }
    }


    public void runTimer() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override                       //von BroCode - warum @Override?
            public void run() {
                System.out.println(time); 
                time--;
                if (time <= 0) {
                    checkEndConditions();
                    timer.cancel();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, time);
    }

    ////////////////////////// GETTER //////////////////////////////
    
    public Player getWinner() {return winner;}
    public Player getLooser() {return looser;}
    public boolean isFinished() {return finished;} //überflüssig aufgrund notify??

}
