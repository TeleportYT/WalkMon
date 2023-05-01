package com.vik.test;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class musicService  extends Service {

    private final IBinder binder = new LocalBinder();


    MediaPlayer player;
    ArrayList<Integer> clips = new ArrayList<Integer>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return this.binder;
    }


    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        this.clips.add(R.raw.mainmenu);
        this.player = new MediaPlayer();
        this.player = MediaPlayer.create(getApplicationContext(),this.clips.get(0));
        this.player.start();
        this.player.setLooping(true);
        return 0;
    }


    public void pauseMusic(){
        this.player.pause();
    }

    public void ResumeMusic(){
        this.player.start();
    }

    public void ChangeMusic(int resource){
        this.player.stop();
        this.player = MediaPlayer.create(getApplicationContext(),resource);
        this.player.start();
    }







    public void onDestroy(){
        if (this.player.isPlaying()){
            this.player.stop();
        }
        this.player.release();
    }


    public class LocalBinder extends Binder {
        musicService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return musicService.this;
        }
    }

}



