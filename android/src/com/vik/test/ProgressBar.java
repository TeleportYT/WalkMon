package com.vik.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ProgressBar {

    private Image background;
    private Image bar;
    private float value=0f;
    private float max;
    private float min;

    public ProgressBar(float max, float min, int height, int width, int positionX, int positionY, Stage st) {
        this.max = max;
        this.min = min;
        background = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("emptyBar.png")))));

        background.setSize(width,height);

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
        pixmap.dispose();


        bar = new Image(drawable);

        bar.setPosition(positionX,positionY);
        background.setPosition(positionX,positionY);

        st.addActor(bar);
        st.addActor(background);


    }

    public void changeValue(float val){
        this.value = val;
    }



}
