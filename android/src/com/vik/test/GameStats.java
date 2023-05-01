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
        this.playerHealth = 100;
        this.playerScore = 0;
        this.enemiesKilled = 0;
        this.time = "";
        this.state = GameState.InGame;
    }

    public float getPlayerHealth() {
        return this.playerHealth;
    }

    public void setPlayerHealth(float health) {
        this.playerHealth = health;
    }

    public float getPlayerScore() {
        return this.playerScore;
    }

    public void addPlayerScore(float score) {
        this.playerScore += score;
    }

    public int getEnemiesKilled() {
        return this.enemiesKilled;
    }

    public void incrementEnemiesKilled() {
        this.enemiesKilled++;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public GameState getState() {
        return this.state;
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
