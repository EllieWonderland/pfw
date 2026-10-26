package de.cipherchase.cipherchase.game;

import java.util.Random;

/**
 * @version 1.0 - 2026-10-13
 * @author Manuel
 * Contains Code for a single door,creates a random int[] with numbers four numbers between 1-6, 
 */
public class SecretCode {
    
    /**
     * Contains der int[] of the door-code
     */
    private final int[] singleCode;

    /**
     * Creates a new Code-Object and initalizes the intern array
     */
    public SecretCode() {
        singleCode = new int[4];
        Random random = new Random();
        for (int i = 0; i <= 3; i++) {
            singleCode[i] = random.nextInt(6) + 1;
        }
    }

    /**
     * Test-Constructor which can be given 4 single numbers, choosen by Users
     * @param test takes a int[4]
     */
    public SecretCode(int[] test) {
        if (test == null) {throw new IllegalArgumentException ("The Array is empty");}
        if (test.length != 4) {throw new IllegalArgumentException ("Only 4 numbers allowed");}
        for (int i = 0; i <= test.length-1; i++) {
            if (test[i] < 1 || test[i] > 6) {
                throw new IllegalArgumentException("Only numbers between 1 and 6 allowed");
            }
        }
        singleCode = test.clone();

    }


    /**
     * Returns a copy of the intern door-Code
     * @return a int[] with four integer
     */
    public int[] getCode() {
        return singleCode.clone();
    }
}
