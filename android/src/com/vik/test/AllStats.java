package com.vik.test;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class AllStats extends Activity {

    GameStatsDbHelper dbHelper;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_stats);

        dbHelper = new GameStatsDbHelper(getApplicationContext());



        List<GameStats> Allstats = dbHelper.getAllGameStats();
        StatsAdpter adapter = new StatsAdpter(this, Allstats);
        ListView lt = findViewById(R.id.lt);
        lt.setAdapter(adapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((TextView)findViewById(R.id.textView3)).setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }


        ((Button)findViewById(R.id.button7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent nt = new Intent(getApplicationContext(),AndroidLauncher.class);
                startActivity(nt);
            }
        });



    }


}
