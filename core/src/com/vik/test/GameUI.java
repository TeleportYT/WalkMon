package com.vik.test;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameUI {

    private Stage st;
    private Skin skin;
    public Touchpad th;
    public Button shotBt;
    public Image crossair;

    public GameUI(){
       this.st = new Stage(new ScreenViewport());
       this.skin = new Skin(Gdx.files.internal("uiskin.json"));
       Skin mySkin = new Skin(Gdx.files.internal("fire_button.json"));
       this.th = new Touchpad(0f,skin.get(Touchpad.TouchpadStyle.class));
       th.setSize(500,500);
       th.setBounds(10,10,500,500);

       this.shotBt = new Button(mySkin.get(Button.ButtonStyle.class));
       shotBt.setSize(250f,250f);
       shotBt.setPosition(st.getWidth()-375f,125f);

       crossair = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("crossair.png")))));
       crossair.setSize(250f,250f);
       crossair.setPosition(st.getWidth()/2-125f,st.getHeight()/2-125f);



        st.addActor(crossair);
        st.addActor(th);
        st.addActor(shotBt);
        st.draw();
    }

    public Stage GetStage(){
        return this.st;
    }


}
