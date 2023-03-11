package com.vik.test;

import android.provider.BaseColumns;

public final class GameStatsContract {
    private GameStatsContract() {}

    public static class GameStatsEntry implements BaseColumns {
        public static final String TABLE_NAME = "gamestats";
        public static final String COLUMN_NAME_PLAYER_HEALTH = "player_health";
        public static final String COLUMN_NAME_PLAYER_SCORE = "player_score";
        public static final String COLUMN_NAME_ENEMIES_KILLED = "enemies_killed";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_STATE = "state";
    }
}
