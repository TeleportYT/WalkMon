package com.vik.test;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class GameLayout extends AndroidApplication {

    private TextView loadingTxt;
    private LottieAnimationView loadingAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_l);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;

        loadingAnim = findViewById(R.id.animationView);
        loadingTxt = findViewById(R.id.textView);
        ConstraintLayout l = (ConstraintLayout) findViewById(R.id.rt);
        LoadGame(l);
    }

    public void LoadGame(ConstraintLayout l){
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        loadingTxt.setVisibility(View.VISIBLE);
        loadingAnim.setVisibility(View.VISIBLE);
        GameManager game = new GameManager(getContext());
        l.addView(initializeForView(game,config));

        Handler mainHandler = new Handler(this.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(!game.isLoaded()){
                    Log.d("Loading","Still Loading");
                    loadingTxt.setText("Loading"+( new String(new char[i]). replace("\0", "*")));
                    i++;
                }
                loadingTxt.setVisibility(View.INVISIBLE);
                loadingAnim.setVisibility(View.INVISIBLE);
            }
        };
        mainHandler.post(myRunnable);
    }
}