package com.vik.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class GameStats {
    private float playerHealth;

    public void setPlayerScore(float playerScore) {
        this.playerScore = playerScore;
    }

    public void setEnemiesKilled(int enemiesKilled) {
        this.enemiesKilled = enemiesKilled;
    }

    private float playerScore;
    private int enemiesKilled;
    private String time;
    private GameState state;

    public GameStats() {
        playerHealth = 100;
        playerScore = 0;
        enemiesKilled = 0;
        time = "";
        state = GameState.InGame;
    }

    public float getPlayerHealth() {
        return playerHealth;
    }

    public void setPlayerHealth(float health) {
        playerHealth = health;
    }

    public float getPlayerScore() {
        return playerScore;
    }

    public void addPlayerScore(float score) {
        playerScore += score;
    }

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public void incrementEnemiesKilled() {
        enemiesKilled++;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }


    public long FinishGame(GameState state, Context ct){
        this.state = state;

        GameStatsDbHelper dbHelper = new GameStatsDbHelper(ct);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_HEALTH, this.playerHealth);
        values.put(GameStatsContract.GameStatsEntry.COLUMN_NAME_PLAYER_SCORE, this.playerScore);
        values.put(GameStatsContract.GameStatsEntry.COLUMN_NAME_ENEMIES_KILLED, this.enemiesKilled);
        values.put(GameStatsContract.GameStatsEntry.COLUMN_NAME_TIME, this.time);
        values.put(GameStatsContract.GameStatsEntry.COLUMN_NAME_STATE, this.state.toString());

        return db.insert(GameStatsContract.GameStatsEntry.TABLE_NAME, null, values);

    }


}
