package com.vik.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

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
        this.skin = Game.manager.get("uiskin.json",Skin.class);
        Skin mySkin = Game.manager.get("fire_button.json",Skin.class);

        this.th = new Touchpad(0f,this.skin.get(Touchpad.TouchpadStyle.class));
        this.th.setSize(this.st.getHeight()/2.5f,this.st.getHeight()/2.5f);
        this.th.setBounds(10,10,this.st.getHeight()/2.5f,this.st.getHeight()/2.5f);


        this.shotBt = new Button(mySkin.get(Button.ButtonStyle.class));
        this.shotBt.setSize(this.st.getHeight()/4,this.st.getHeight()/4);
        this.shotBt.setPosition(this.st.getWidth()-this.st.getHeight()/3 ,this.st.getHeight()/8);
        this.shotBt.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                Game.pc.Fire();
                return  true;
            }
        });




        this.crossair = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("crossair.png")))));
        this.crossair.setSize(this.st.getHeight()/4,st.getHeight()/4);
        this.crossair.setPosition(this.st.getWidth()/2-st.getHeight()/8,this.st.getHeight()/2-this.st.getHeight()/8);
        this.crossair.setTouchable(Touchable.disabled);

        BloodEffect = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("bloodEffect.png")))));
        BloodEffect.setColor(255,255,255,0);
        BloodEffect.setSize(this.st.getWidth(),this.st.getHeight());
        BloodEffect.setTouchable(Touchable.disabled);
        Texture  myTexture = new Texture(Gdx.files.internal("buttonSet.png"));
        TextureRegion myTextureRegion = new TextureRegion(myTexture);
        TextureRegionDrawable myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);


        ImageButton button = new ImageButton(myTexRegionDrawable); //Set the button up
        button.setSize(this.st.getHeight()/6,this.st.getHeight()/6);
        button.setPosition(this.st.getWidth()-button.getWidth(),this.st.getHeight()-button.getHeight());
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
                ((AndroidApplication) Gdx.app).getContext().sendBroadcast(intent);
            };
        });

        // MakeDialog();

        this.menu = new Menu("Settings",this.skin,this.st);
        this.menu.setVisible(false);
        this.st.addActor(menu.getTable());
        this.st.addActor(BloodEffect);
        this.st.addActor(crossair);
        this.st.addActor(th);
        this.st.addActor(shotBt);
        this.st.addActor(button);


        this.st.draw();
    }




    public void Update(){
        this.BloodEffect.setColor(255,255,255,1-(Game.pc.hp/100));
        this.st.act(Gdx.graphics.getDeltaTime());
        this.st.draw();
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
