package com.vik.test;

import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class LoadingScreen implements Screen {
    Stage st;
    private final BitmapFont font;
    private final SpriteBatch batch;
    private final float progressBarWidth = 400;
    private final float progressBarHeight = 20;
    private final float progressBarX = (Gdx.graphics.getWidth() - progressBarWidth) / 2;
    private final float progressBarY = (Gdx.graphics.getHeight() - progressBarHeight) / 2;

    public LoadingScreen() {
        this.font = new BitmapFont();
        this.batch = new SpriteBatch();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Log.d("Load","loading");
        batch.begin();
        font.draw(batch,"Loading...",progressBarX + progressBarWidth / 2, progressBarY + progressBarHeight + 20);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
