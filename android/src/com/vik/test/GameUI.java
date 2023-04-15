package com.vik.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import org.w3c.dom.Text;

public class GameUI {

    public  Stage getSt() {
        return st;
    }

    public static Stage st;
    private Skin skin;
    public Touchpad th;
    public Image crossair;
    public Image BloodEffect;
    public Button shotBt;
    public boolean isMenu=false;
    public Menu menu;

    public GameUI(){
        IntentFilter intentFilter = new IntentFilter("Player Damaged");
        intentFilter.addAction("Player Dead");
        intentFilter.addAction("Pause Game");
        intentFilter.addAction("Player Healed");
        ((AndroidApplication) Gdx.app).getContext().registerReceiver(receiver,intentFilter);

       this.st = new Stage(new ScreenViewport());
       this.skin = MyClass.manager.get("uiskin.json",Skin.class);
       Skin mySkin = MyClass.manager.get("fire_button.json",Skin.class);

       this.th = new Touchpad(0f,skin.get(Touchpad.TouchpadStyle.class));
       th.setSize(st.getHeight()/2.5f,st.getHeight()/2.5f);
       th.setBounds(10,10,st.getHeight()/2.5f,st.getHeight()/2.5f);


        this.shotBt = new Button(mySkin.get(Button.ButtonStyle.class));
        shotBt.setSize(st.getHeight()/4,st.getHeight()/4);
        shotBt.setPosition(st.getWidth()-st.getHeight()/3 ,st.getHeight()/8);
        shotBt.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                MyClass.pc.Fire();
                return  true;
            }
        });




       crossair = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("crossair.png")))));
       crossair.setSize(st.getHeight()/4,st.getHeight()/4);
       crossair.setPosition(st.getWidth()/2-st.getHeight()/8,st.getHeight()/2-st.getHeight()/8);
       crossair.setTouchable(Touchable.disabled);

       BloodEffect = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("bloodEffect.png")))));
       BloodEffect.setColor(255,255,255,0);
       BloodEffect.setSize(st.getWidth(),st.getHeight());
       BloodEffect.setTouchable(Touchable.disabled);
        Texture  myTexture = new Texture(Gdx.files.internal("buttonSet.png"));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);


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

       // MakeDialog();

         menu = new Menu("Settings",skin,st);
         menu.setVisible(false);
        st.addActor(BloodEffect);
        st.addActor(crossair);
        st.addActor(th);
        st.addActor(shotBt);
        st.addActor(button);
        st.addActor(menu.getTable());

        st.draw();
    }




    public void Update(){
        BloodEffect.setColor(255,255,255,1-(MyClass.pc.hp/100));
        st.act(Gdx.graphics.getDeltaTime());
        st.draw();
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
