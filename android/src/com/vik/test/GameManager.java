package com.vik.test;

import android.content.Context;

import com.badlogic.gdx.Gdx;

public class GameManager extends com.badlogic.gdx.Game {

    @Override
    public void create() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               Game game  = new Game(ct);
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
    Context ct;

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
            return;
        }

        if(cl.isLoaded() && !isFirst){
            isFirst = true;
            setScreen(cl);
            cl.show();

        }
        else if (cl.isLoaded()){
            cl.render(Gdx.graphics.getDeltaTime());
        }
        else{
            Game.manager.update();
        }
    }

    public boolean isLoaded() {
        if(cl == null){
            return false;
        }
        return cl.isLoaded();
    }
}
