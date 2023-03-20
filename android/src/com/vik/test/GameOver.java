package com.vik.test;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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



        Resources res = getResources();
        for (int i =2 ; i<5;i++){
            int ifF = res.getIdentifier("txt"+(i), "id", getApplicationContext().getPackageName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ((TextView)findViewById(ifF)).setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            }
            ((TextView)findViewById(ifF)).setLines(1);
        }



        for (int i =4 ; i<7;i++){
            int ifF = res.getIdentifier("button"+(i), "id", getApplicationContext().getPackageName());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ((Button)findViewById(ifF)).setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            }
            ((Button)findViewById(ifF)).setLines(1);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((TextView)findViewById(R.id.score)).setAutoSizeTextTypeWithDefaults(Button.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }


    }

    private void ShowStats(int id){
        GameStatsDbHelper dbHelper = new GameStatsDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] data = dbHelper.GetData(id);
        ((TextView)findViewById(R.id.score)).setText(data[1]);
        ((TextView)findViewById(R.id.time)).setText(data[3]);
        ((TextView)findViewById(R.id.killed)).setText(data[2]);
        ((TextView)findViewById(R.id.hp)).setText(data[0]);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((TextView)findViewById(R.id.time)).setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            ((TextView)findViewById(R.id.killed)).setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            ((TextView)findViewById(R.id.hp)).setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
    }

    public void OnClick(View v) {
        Intent nt = new Intent(this,AndroidLauncher.class);
        startActivity(nt);
    }
}
