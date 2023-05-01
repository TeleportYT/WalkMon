package com.vik.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;

public class MyFPS extends FirstPersonCameraController {

    public void setPl(PlayerController pl) {
        this.pl = pl;
    }

    PlayerController pl;
    public MyFPS(Camera camera) {
        super(camera);
        IntentFilter intentFilter = new IntentFilter("Pause Game");
        ((AndroidApplication) Gdx.app).getContext().registerReceiver(receiver,intentFilter);
    }



    boolean isPaused = false;

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        if (!this.isPaused){
            Log.d("Touch","Y: "+screenY+" x: "+Gdx.input.getX(1)+" pointer: "+pointer);
            float deltaX = -Gdx.input.getDeltaX(pointer) * degreesPerPixel/2;
            float deltaY = -Gdx.input.getDeltaY(pointer) * degreesPerPixel/2;
            this.camera.direction.rotate(this.camera.up, deltaX);
            this.tmp.set(this.camera.direction).crs(this.camera.up).nor();
            this.camera.direction.rotate(this.tmp, deltaY);
        }
        return true;
    }

    //region Broadcast Receiver
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isPaused = intent.getBooleanExtra("isPaused",isPaused);
        }
    };
    //endregion



}
