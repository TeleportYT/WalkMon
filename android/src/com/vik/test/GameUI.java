package com.vik.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class GameUI {

    public static Stage st;
    private Skin skin;
    public Touchpad th;
    public Button shotBt;
    public Image crossair;
    public Image BloodEffect;
    public Image Loading;

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

       this.shotBt = new Button(mySkin.get(Button.ButtonStyle.class));
       shotBt.setSize(st.getHeight()/4,st.getHeight()/4);
       shotBt.setPosition(st.getWidth()-st.getHeight()/3 ,st.getHeight()/8);

       crossair = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("crossair.png")))));
       crossair.setSize(st.getHeight()/4,st.getHeight()/4);
       crossair.setPosition(st.getWidth()/2-st.getHeight()/8,st.getHeight()/2-st.getHeight()/8);

       BloodEffect = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("bloodEffect.png")))));
       BloodEffect.setColor(255,255,255,0);
       BloodEffect.setSize(st.getWidth(),st.getHeight());


        st.addActor(BloodEffect);
        st.addActor(crossair);
        st.addActor(th);
        st.addActor(shotBt);
        st.draw();
    }

    public void Update(){
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        Gdx.app.debug("Game UI","Player hp is: "+MyClass.pc.hp);

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
