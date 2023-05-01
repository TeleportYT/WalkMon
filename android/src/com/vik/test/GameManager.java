package com.vik.test;

import android.content.Context;

import com.badlogic.gdx.Gdx;

public class GameManager extends com.badlogic.gdx.Game {

    @Override
    public void create() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Game game  = new Game();
                game.Load();
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

    Game cl;

    public GameManager() {

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

        if(this.cl == null){
            return;
        }

        if(this.cl.isLoaded() && !this.isFirst){
            this.isFirst = true;
            setScreen(this.cl);
            this.cl.show();

        }
        else if (this.cl.isLoaded()){
            this.cl.render(Gdx.graphics.getDeltaTime());
        }
        else{
            Game.manager.update();
        }
    }

    public boolean isLoaded() {
        if(this.cl == null){
            return false;
        }
        return this.cl.isLoaded();
    }
}
