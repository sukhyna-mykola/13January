package com.game.kolas.mygame;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by mykola on 25.12.16.
 */
public class GameSurfaceTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();


    }

    public void testStart() throws Exception {
      String res = "1:10";
        assertEquals(res.split(":")[1],"10");
        assertEquals(StringToInt(res),23);
    }
    private int StringToInt(String s) {
        int res = 0;
        String[] list = s.split(":");
        Collections.reverse(Arrays.asList(list));
        for (int i =0; i <list.length; i++) {
            res +=  ((int) Math.pow(60, i) * Integer.parseInt(list[i]));
        }
        return res;
    }
}