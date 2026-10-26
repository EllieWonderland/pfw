package de.cipherchase.cipherchase.game;

import java.util.ArrayList;


/**
 * @version 1.0 - 2026-10-25
 * @author Manuel
 * Represents a single Door in a Gamesession
 */
public class Door {
    
    private static int doorCounter = 0;
    private final int number;

    private SecretCode code;
    private boolean alreadyOpened;

    
    public ArrayList<int[]> failedCodeTraces = new ArrayList<>();

    


    public Door() {
        doorCounter++;
        number = doorCounter;

        this.code = new SecretCode();
        this.alreadyOpened = false;
    }



    public int getDoorNumber() {
        return number;
    }
    
    public boolean alreadyOpened() {
        return alreadyOpened;
    }

    public int[] getDoorCode() {
        return this.code.getCode();
    }




    /*

    public void traceRemoved() {
        SecretCode NewDoorCode = new SecretCode();
        int[] code = NewDoorCode.getCode();
    }
    
    */



}
