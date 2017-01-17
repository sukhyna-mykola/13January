package com.game.kolas.mygame;

import junit.framework.TestCase;

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
        assertEquals(GameSurface.StringToInt(res),23);
    }

}