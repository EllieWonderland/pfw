package de.cipherchase.cipherchase;

import de.cipherchase.cipherchase.game.SecretCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


// neu schreiben, 

/**
 *
 * CodeTest, tests if the code has been processed corretly
 */
public class SecretCodeTest {

    @Test
    void acceptsValidCode() {
        int[] expected = {1, 2, 3, 6};
        SecretCode code = new SecretCode(expected);
        assertArrayEquals(expected, code.getCode());
    }

    @Test
    void acceptsRepeatedNumbers() {
        int[] expected = {1, 1, 6, 6};
        SecretCode code = new SecretCode(expected);
        assertArrayEquals(expected, code.getCode());
    }

    @Test
    void rejectsZero() {
        assertThrows(IllegalArgumentException.class,
        () -> new SecretCode(new int[]{0, 2, 3, 6}));
    }

    @Test
    void rejectsNegativeNumber() {
        assertThrows(IllegalArgumentException.class,
        () -> new SecretCode(new int[]{1, 2, 3, -6}));
    }

    @Test
    void rejectsNumberAboveSix() {
        assertThrows(IllegalArgumentException.class,
        () -> new SecretCode(new int[]{1, 2, 3, 7}));
    }

    @Test
    void rejectsTooShortCode() {
        assertThrows(IllegalArgumentException.class,
        () -> new SecretCode(new int[]{1, 2, 3}));
    }

    @Test
    void rejectsTooLongCode() {
        assertThrows(IllegalArgumentException.class,
        () -> new SecretCode(new int[]{1, 2, 3, 4, 5}));
    }

    @Test
    void rejectsNull() {
        assertThrows(IllegalArgumentException.class,
        () -> new SecretCode(null));
    }

    @Test
    void protectsInternalArray() {
        int[] input = {1, 2, 3, 4};
        SecretCode code = new SecretCode(input);

        input[0] = 6;
        assertArrayEquals(new int[]{1, 2, 3, 4}, code.getCode());

        int[] returned = code.getCode();
        returned[1] = 6;
        assertArrayEquals(new int[]{1, 2, 3, 4}, code.getCode());
    }
}
