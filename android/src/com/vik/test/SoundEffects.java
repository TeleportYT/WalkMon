package com.vik.test;


import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.android.AndroidApplication;

import Enemys.EnemyType;

public class SoundEffects {

    private Music walking;
    private Music enemy;
    private Music Damaged;
    private Music inGame;
    private AssetManager manager;
    private long walkingId;
    SharedPreferences prefs;
    SharedPreferences.Editor myEdit;
    public SoundEffects(AssetManager manager) {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(((AndroidApplication) Gdx.app).getContext());




        myEdit = prefs.edit();

        this.walking = manager.get("sound effects/running.mp3");
        walking.setLooping(true);

        this.inGame = manager.get("sound effects/inGameMusic.mp3");
        this.inGame.play();
        this.inGame.setLooping(true);

        IntentFilter intentFilter = new IntentFilter("Pause Game");
        intentFilter.addAction("Player Damaged");
        ((AndroidApplication) Gdx.app).getContext().registerReceiver(receiver,intentFilter);


        this.manager = manager;

        LowerSoundEffects(prefs.getFloat("SoundEffect",100));
        VolumeMusic(prefs.getFloat("Music",100));


    }

    public void Walk(boolean isWalking){
        if(isWalking){
            if(!walking.isPlaying()){
                walking.play();
            }
        }
        else{
            walking.pause();
        }
    }


    public void GettingDamaged(){

    }



    public void LowerSoundEffects(float volume){
        if(walking!=null){
            walking.setVolume(volume/100);
            SEvolume = volume/100;
        }

        myEdit.putFloat("SoundEffect",volume);
        myEdit.apply();
    }

    public void VolumeMusic(float volume){
        inGame.setVolume(volume/100);
        myEdit.putFloat("Music",volume);
        myEdit.apply();
    }


    private float SEvolume= 1f;


    boolean isPaused;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           if(intent.getAction().equals("Pause Game")){
               if(intent.getBooleanExtra("isPaused",isPaused)){
                   walking.pause();
               }
           }
           else if(intent.getAction().equals("Player Damaged")){
               Sound s;
               switch (intent.getStringExtra("Type")){
                   case "bob":
                        s = manager.get("sound effects/flyingEnemy.mp3");
                       s.play(SEvolume);
                       break;
                   case "warrior":
                        s = manager.get("sound effects/punch.mp3");
                       s.play(SEvolume);
                       break;
               }


           }
        }
    };


    public void Shoot(){
        Sound s = manager.get("sound effects/shoot.mp3");
        s.play(SEvolume);
    }



    public void  dispose(){
        walking.dispose();
    }

}
