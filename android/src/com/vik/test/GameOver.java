package com.vik.test;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;

import java.util.ArrayList;

public class GameOver extends Activity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        Intent nt = getIntent();
        int id = Integer.parseInt(nt.getStringExtra("StatsId"));
        ShowStats(id);



        ((Button) findViewById(R.id.button5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent db = new Intent(getApplicationContext(),AllStats.class);
                startActivity(db);
            }
        });

        ((Button) findViewById(R.id.button4)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nt = new Intent(getApplicationContext(),GameLayout.class);
                finish();
                startActivity(nt);
            }
        });


        ((Button) findViewById(R.id.button6)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nt = new Intent(getApplicationContext(),AndroidLauncher.class);
                finish();
                startActivity(nt);
            }
        });

    }

    private void ShowStats(int id){
        GameStatsDbHelper dbHelper = new GameStatsDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] data = dbHelper.GetData(id);
        ((TextView)findViewById(R.id.score)).setText(data[1]);
        ((TextView)findViewById(R.id.time)).setText(data[3]);
        ((TextView)findViewById(R.id.killed)).setText(data[2]);
        ((TextView)findViewById(R.id.hp)).setText(data[0]);
    }

    public void OnClick(View v) {
        Intent nt = new Intent(this,AndroidLauncher.class);
        startActivity(nt);
    }
}
