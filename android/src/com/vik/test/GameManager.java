package com.vik.test;

import android.content.Context;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class GameManager extends Game {

    @Override
    public void create() {
        setScreen(lt);

        new Thread(new Runnable() {
            @Override
            public void run() {
                MyClass game = new MyClass(ct);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                       cl = game;
                    }
                });
            }
        }).start();

    }
    public boolean isFirst = false;

    MyClass cl;
    Context ct;

    public GameManager(LoadingScreen lt) {
        this.lt = lt;
    }

    LoadingScreen lt;

    public GameManager(Context ct) {
        this.ct = ct;
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void render() {

        if(cl == null){
            lt.render(Gdx.graphics.getDeltaTime());
            return;
        }


        if(cl.isRunning() && isFirst){
           setScreen(cl);
           cl.show();
        }
        else if (cl.isRunning() && !isFirst){
            cl.render(Gdx.graphics.getDeltaTime());
        }
        else{
            lt.render(Gdx.graphics.getDeltaTime());
        }
    }
}
