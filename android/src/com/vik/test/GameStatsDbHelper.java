package com.vik.test;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

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

    @SuppressLint("Range")
    public String[] GetData(int StatsId){
        String[] info = new String[6];

        String sql = "SELECT * FROM gamestats WHERE _id = ?";

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, new String[] { String.valueOf(StatsId) });

        if (cursor.moveToFirst()) {
            // get the data from the row
            info[0] = cursor.getString(cursor.getColumnIndex(GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_HEALTH));
            info[1] = cursor.getString(cursor.getColumnIndex(GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_SCORE ));
            info[2] = cursor.getString(cursor.getColumnIndex(GameStatsContract.GameStatsEntry.COLUMN_NAME_ENEMIES_KILLED));
            info[3] = cursor.getString(cursor.getColumnIndex(GameStatsContract.GameStatsEntry.COLUMN_NAME_TIME ));
            info[4] = cursor.getString(cursor.getColumnIndex(GameStatsContract.GameStatsEntry.COLUMN_NAME_STATE));
            // and so on for other columns
        }

        cursor.close();

        return info;

    }



    public List<GameStats> getAllGameStats() {
        List<GameStats> gameStatsList = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        String[] projection = {
                GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_HEALTH,
                GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_SCORE,
                GameStatsContract.GameStatsEntry.COLUMN_NAME_ENEMIES_KILLED,
                GameStatsContract.GameStatsEntry.COLUMN_NAME_TIME,
                GameStatsContract.GameStatsEntry.COLUMN_NAME_STATE
        };

        String sortOrder = GameStatsContract.GameStatsEntry._ID + " ASC";

        Cursor cursor = db.query(
                GameStatsContract.GameStatsEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        while (cursor.moveToNext()) {
            GameStats gameStats = new GameStats();
            gameStats.setPlayerHealth(cursor.getFloat(cursor.getColumnIndexOrThrow(GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_HEALTH)));
            gameStats.setPlayerScore(cursor.getFloat(cursor.getColumnIndexOrThrow(GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_SCORE)));
            gameStats.setEnemiesKilled(cursor.getInt(cursor.getColumnIndexOrThrow(GameStatsContract.GameStatsEntry.COLUMN_NAME_ENEMIES_KILLED)));
            gameStats.setTime(cursor.getString(cursor.getColumnIndexOrThrow(GameStatsContract.GameStatsEntry.COLUMN_NAME_TIME)));
            gameStats.setState(GameState.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(GameStatsContract.GameStatsEntry.COLUMN_NAME_STATE))));

            gameStatsList.add(gameStats);
        }

        cursor.close();

        return gameStatsList;
    }


}
