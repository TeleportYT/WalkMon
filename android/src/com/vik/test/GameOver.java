package com.vik.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.badlogic.gdx.backends.android.AndroidApplication;

import java.util.ArrayList;

public class GameOver extends Activity {
    private GameStats stats;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        ((Button) findViewById(R.id.button4)).setOnClickListener(this::OnClick);
    }

    public void OnClick(View v) {
        Intent nt = new Intent(this,AndroidLauncher.class);
        startActivity(nt);
    }
}
