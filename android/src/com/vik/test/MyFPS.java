package com.vik.test;

import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;

public class MyFPS extends FirstPersonCameraController {

    public void setPl(PlayerController pl) {
        this.pl = pl;
    }

    PlayerController pl;
    public MyFPS(Camera camera) {
        super(camera);

    }



    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        Log.d("Touch","Y: "+screenY+" x: "+Gdx.input.getX(1)+" pointer: "+pointer);
        float deltaX = -Gdx.input.getDeltaX(pointer) * degreesPerPixel/2;
        float deltaY = -Gdx.input.getDeltaY(pointer) * degreesPerPixel/2;
        camera.direction.rotate(camera.up, deltaX);
        tmp.set(camera.direction).crs(camera.up).nor();
        camera.direction.rotate(tmp, deltaY);
        pl.Fire();
        return true;
    }

}
