package com.bosicc.cluedo.utils;

import java.io.Serializable;

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
    
    public static class Coord implements Serializable{
        private static final long serialVersionUID = 5690404645597703832L;
        public int pos = 0;
        public int num = 0;
    }
}
