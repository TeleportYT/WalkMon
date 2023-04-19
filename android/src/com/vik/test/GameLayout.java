package com.vik.test;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class GameLayout extends AndroidApplication {

    private TextView loadingTxt;
    private LottieAnimationView loadingAnim;
    private ImageView bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_l);
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;

        bg= findViewById(R.id.imageView);
        loadingAnim = findViewById(R.id.animationView);
        loadingTxt = findViewById(R.id.textView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            loadingTxt.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
        ConstraintLayout l = (ConstraintLayout) findViewById(R.id.rt);
        LoadGame(l);



    }





    public void LoadGame(ConstraintLayout l) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useAccelerometer = false;
        config.useCompass = false;
        loadingTxt.setVisibility(View.VISIBLE);
        loadingAnim.setVisibility(View.VISIBLE);
        GameManager game = new GameManager(getContext());
        View v = initializeForView(game, config);
        l.addView(v);
       updateLoadingText(game);
    }


    public void updateLoadingText(GameManager gameManager) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (!gameManager.isLoaded()) {

                    final String loadingMessage = "Loading"+new String(new char[i]). replace("\0", ".");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            loadingAnim.setElevation(1);
                            bg.setElevation(0.5f);
                            loadingTxt.setText(loadingMessage);
                        }
                    });
                    i++;
                    if(i>3){
                        i=0;
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        loadingTxt.setVisibility(View.INVISIBLE);
                        loadingAnim.setVisibility(View.INVISIBLE);
                        bg.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).start();
    }

}
