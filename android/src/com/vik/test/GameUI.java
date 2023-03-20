package com.vik.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class GameUI {

    public static Stage st;
    private Skin skin;
    public Touchpad th;
    public Image crossair;
    public Image BloodEffect;
    public boolean isMenu=false;
    public Table menu;

    public GameUI(){
        IntentFilter intentFilter = new IntentFilter("Player Damaged");
        intentFilter.addAction("Player Dead");
        intentFilter.addAction("Pause Game");
        intentFilter.addAction("Player Healed");
        MyClass.context.registerReceiver(receiver,intentFilter);

       this.st = new Stage(new ScreenViewport());
       this.skin = MyClass.manager.get("uiskin.json",Skin.class);
       Skin mySkin = MyClass.manager.get("fire_button.json",Skin.class);

       this.th = new Touchpad(0f,skin.get(Touchpad.TouchpadStyle.class));
       th.setSize(st.getHeight()/2.5f,st.getHeight()/2.5f);
       th.setBounds(10,10,st.getHeight()/2.5f,st.getHeight()/2.5f);

       crossair = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("crossair.png")))));
       crossair.setSize(st.getHeight()/4,st.getHeight()/4);
       crossair.setPosition(st.getWidth()/2-st.getHeight()/8,st.getHeight()/2-st.getHeight()/8);

       BloodEffect = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("bloodEffect.png")))));
       BloodEffect.setColor(255,255,255,0);
       BloodEffect.setSize(st.getWidth(),st.getHeight());

        Texture  myTexture = new Texture(Gdx.files.internal("buttonSet.png"));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);

        menu = new Table();
        menu.setVisible(false);
        ImageButton button = new ImageButton(myTexRegionDrawable); //Set the button up
        button.setSize(st.getHeight()/6,st.getHeight()/6);
        button.setPosition(st.getWidth()-button.getWidth(),st.getHeight()-button.getHeight());
        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Log.d("Pressed","pressing");

                if (isMenu){
                    menu.setVisible(false);
                    crossair.setVisible(true);
                }
                else {
                    menu.setVisible(true);
                    crossair.setVisible(false);
                }
                isMenu = !isMenu;
                Intent intent=new Intent("Pause Game");
                intent.putExtra("isPaused", isMenu);
                MyClass.context.sendBroadcast(intent);
            };
        });

        MakeMenu();

        menu.setSize(st.getWidth()/1.5f,st.getHeight()/1.5f);
        menu.setPosition(st.getWidth()/2-menu.getWidth()/2,st.getHeight()/2-menu.getHeight()/2);


        st.addActor(BloodEffect);
        st.addActor(crossair);
        st.addActor(th);
        st.addActor(button);
        st.draw();
    }


    public void MakeMenu(){
        Label label = new Label("Pause", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        label.setAlignment(Align.center);
        label.setFontScale(5);
        menu.add(label).size(st.getWidth()/1.5f,st.getHeight()/(1.5f*3)).padBottom(20).row();

        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menu.setVisible(false);
                crossair.setVisible(true);
                isMenu = true;
                Intent intent=new Intent("Pause Game");
                intent.putExtra("isPaused", isMenu);
                MyClass.context.sendBroadcast(intent);
            }
        });

        menu.add(resumeButton).size(st.getWidth()/1.5f,st.getHeight()/(1.5f*3)).padBottom(20).row();


        TextButton exitButton = new TextButton("Exit", skin);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });
        menu.add(exitButton).size(st.getWidth()/1.5f,st.getHeight()/(1.5f*3)).row();

        menu.setSize(st.getWidth()/1.5f,st.getHeight()/1.5f);
        menu.setPosition(st.getWidth()/2-menu.getWidth()/2,st.getHeight()/2-menu.getHeight()/2);

        st.addActor(menu);
    }


    public void Update(){
        BloodEffect.setColor(255,255,255,1-(MyClass.pc.hp/100));
        
    }


    public Stage GetStage(){
        return this.st;
    }


    //region Broadcast Receiver
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("Player Damaged")){

            }
            else if(intent.getAction().equals("Player Dead")){
                Toast.makeText(context,"Player Died",Toast.LENGTH_SHORT);
            }
            else if(intent.getAction().equals("Pause Game")){
            }
            else if(intent.getAction().equals("Player Healed")){

            }
        }
    };
    //endregion


}
