package com.vik.test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameStatsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "GameStats.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + GameStatsContract.GameStatsEntry.TABLE_NAME + " (" +
                    GameStatsContract.GameStatsEntry._ID + " INTEGER PRIMARY KEY," +
                    GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_HEALTH + " FLOAT," +
                    GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_SCORE + " FLOAT," +
                    GameStatsContract.GameStatsEntry.COLUMN_NAME_ENEMIES_KILLED + " INTEGER," +
                    GameStatsContract.GameStatsEntry.COLUMN_NAME_TIME + " TEXT," +
                    GameStatsContract.GameStatsEntry.COLUMN_NAME_STATE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + GameStatsContract.GameStatsEntry.TABLE_NAME;

    public GameStatsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
