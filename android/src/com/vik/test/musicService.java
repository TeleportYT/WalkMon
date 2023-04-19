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
        return binder;
    }


    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        clips.add(R.raw.mainmenu);
        player = new MediaPlayer();
        player = MediaPlayer.create(getApplicationContext(),clips.get(0));
        player.start();
        player.setLooping(true);
        return 0;
    }


    public void pauseMusic(){
        player.pause();
    }

    public void ResumeMusic(){
        player.start();
    }

    public void ChangeMusic(int resource){
        player.stop();
        player = MediaPlayer.create(getApplicationContext(),resource);
        player.start();
    }







    public void onDestroy(){
        if (player.isPlaying()){
            player.stop();
        }
        player.release();
    }


    public class LocalBinder extends Binder {
        musicService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return musicService.this;
        }
    }

}



