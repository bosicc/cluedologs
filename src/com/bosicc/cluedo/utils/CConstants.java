package com.bosicc.cluedo.utils;

/**
 * Global Cluedo constants and definitions.
 */

public final class CConstants {

    /**
     * Action constants for Cluedo Intents
     */
    public static final String ACTION_UPDATE_DATA = "com.bosicc.cluedo.android.intent.action.UPDATE_DATA";

    public static final String FLURRY_KEY = "7NY2JI5ULCANS47CC6S7";
    public static final String FLURRY_GAME_CREATE = "GAMECREATE";
    public static final String FLURRY_GAME_PLAY = "GAMEPLAY";
    public static final String FLURRY_TABLEACTIVITY_RESUME = "TABLEACTIVITY_RESUME";
    public static final String FLURRY_LOADGAME = "LOADGAME";
    
    public class Coord {
        public int pos = 0;
        public int num = 0;
    }
}
