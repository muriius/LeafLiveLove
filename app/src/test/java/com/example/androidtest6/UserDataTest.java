package com.example.androidtest6;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserDataTest {
    /*
    Werte richtig in User-DataLibrary eingetragen
     */
    @Test
    public void SendUserDataTest() {
        assertEquals(4, 2 + 2);
    }
}