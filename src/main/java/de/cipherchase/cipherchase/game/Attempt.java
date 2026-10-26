package de.cipherchase.cipherchase.game;

/**
 * @version 1.0 - 2026-10-13
 * @author Manuel
 * Attempt contains the check mechanism, if a guessed code matches the completely or
 * partly the code of the door.
 * 
 * To-Do: fehlgeschlagene Versuche erzeugen ein neues Traceview Objekt pro Türe
 * vllt. Trace direkt in Door speichern?
 */
public class Attempt {

    private int[] doorCode;
    private int[] guessedCode;
    private boolean[] exactMatches = new boolean[4];
    private boolean[] misplacedMatches = new boolean[4];
    private boolean[] usedDoorPositions = new boolean[4];
    private boolean[] usedGuessPositions = new boolean[4];


    /**
     * Creates a new Attempt Object and checks the matches
     * @param code is the code of the current door
     * @param guessedCode is the guessed array of numbers the player tries at the door
     */
    public Attempt(Door door, int[] guessedCode) {

        this.doorCode = door.getDoorCode();
        this.guessedCode = guessedCode.clone();
        compareCodes();
    }


    /**
     * Test-Constructor which comares a manual choosen testDoorCode int[4] and a manuel choosen guessedCode int[4]
     * @param testDoorCode manual choosen Code for the door
     * @param guessedCode manual choosen guessedCode
     */
    public Attempt (int[] testDoorCode, int[] guessedCode) {
        this.doorCode = testDoorCode.clone();
        this.guessedCode = guessedCode.clone();

        compareCodes();
    }

    private void compareCodes() {
                //checks direct matches
        for(int i=0; i<=3; i++) {
            if (doorCode[i] == this.guessedCode[i]) {
                exactMatches[i] = true;
                usedDoorPositions[i] = true;
                usedGuessPositions[i] = true;
            }
        }

        //checks misplaced matches
        for (int i = 0; i < 4; i++) {
            if (usedDoorPositions[i]) continue;
            for (int v = 0; v < 4; v++) {
                if (usedGuessPositions[v]) continue;
                if (doorCode[i] == this.guessedCode[v]) {
                    misplacedMatches[v] = true;
                    usedDoorPositions[i] = true;
                    usedGuessPositions[v] = true;
                    break;
                }
            }
        }
    }

    /**
     * returns the info if the guessed code matches the door code
     * @return true or false, depending if the guessed code matches the door code
     */
    public boolean isCorrect() {
        for (boolean i:exactMatches) {
            if (i == false) {return false;};
        }
        return true;
    }

    /**
     * Gives a Array which sets true at a position if the guessed number at a specific position matches
     * the the door code at the same position
     * @return a boolean[4] of exact matches
     */
    public boolean[] getExactMatches() {
         return exactMatches.clone();
    }

    /**
     * Gives a array which sets true at a position if the guessed number at this position is part of
     * the door code at a different position
     * @return a boolean[4] of missplaced matches
     */
    public boolean[] getMisplacedMatches() {
        return misplacedMatches.clone();
    }
}
