package de.cipherchase.cipherchase;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

import de.cipherchase.cipherchase.game.Attempt;

/**
 * @version 1.0 - 2026-10-13
 * @author Manuel
 *Tests the Attempt.java class
 */
public class AttemptTest {
    

    @Test 
    public void directMatchAndTwoMissplacedMatches() {
        int[] guessArray = {1, 2, 3, 4};
        int[] doorArray = {1, 4, 2, 2};

        Attempt testAttempt = new Attempt(doorArray, guessArray);
        assertArrayEquals(new boolean[]{true, false, false, false}, testAttempt.getExactMatches());
        assertArrayEquals(new boolean[]{false, true, false, true}, testAttempt.getMisplacedMatches());
    }

    @Test 
    public void twoSameDoorCodesAndGuesses() {
        int[] guessArray = {1, 2, 1, 4};
        int[] doorArray = {3, 1, 5, 1};
 
        Attempt testAttempt = new Attempt(doorArray, guessArray);
        assertArrayEquals(new boolean[]{false, false, false, false}, testAttempt.getExactMatches());
        assertArrayEquals(new boolean[]{true, false, true, false}, testAttempt.getMisplacedMatches());
    }

        @Test 
    public void completeCorrect() {
        int[] guessArray = {1, 2, 3, 4};
        int[] doorArray = {1, 2, 3, 4};
 
        Attempt testAttempt = new Attempt(doorArray, guessArray);
        assertArrayEquals(new boolean[]{true, true, true, true}, testAttempt.getExactMatches());
        assertArrayEquals(new boolean[]{false, false, false, false}, testAttempt.getMisplacedMatches());
    }

        @Test 
    public void completeMissplaced() {
        int[] guessArray = {1, 2, 3, 4};
        int[] doorArray = {4, 3, 2, 1};
 
        Attempt testAttempt = new Attempt(doorArray, guessArray);
        assertArrayEquals(new boolean[]{false, false, false, false}, testAttempt.getExactMatches());
        assertArrayEquals(new boolean[]{true, true, true, true}, testAttempt.getMisplacedMatches());
    }








}
