package com.vik.test;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.opengl.Visibility;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class Menu {

    private Label menuName;

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    private Table table;

    public void setVisible(boolean visible) {
        table.setVisible(visible);
        bg.setVisible(visible);
    }
    Image bg;

    public Menu(String name, Skin skin, Stage stage){

        table = new Table(skin);
        table.setBounds(stage.getWidth()/2-stage.getWidth()/3f,stage.getHeight()/2-stage.getHeight()/3f,stage.getWidth()/1.5f,stage.getHeight()/1.5f);
        table.setVisible(true);
        table.setDebug(true);
        menuName = new Label(name,new Label.LabelStyle(generatefont(), Color.WHITE));
        menuName.setAlignment(Align.center);
        table.add(menuName)
                .expand()
                .fill()
                .colspan(2);

        MakeMenu(PrepareActors(skin),stage);


        bg = new Image(new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("menubg2.png")))));
        bg.setBounds(stage.getWidth()/2-stage.getWidth()/3f-20f,stage.getHeight()/2-stage.getHeight()/3f-25f,stage.getWidth()/1.5f+20f,stage.getHeight()/1.5f+20f);
        bg.setTouchable(Touchable.disabled);
        bg.setVisible(true);

        stage.addActor(bg);
        stage.addActor(table);
    }

    boolean isToggled = true;

    public ArrayList<Actor> PrepareActors(Skin skin){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(((AndroidApplication) Gdx.app).getContext());
        ArrayList<Actor> actors = new ArrayList<Actor>();

        Label msVolume = new Label("Music",new Label.LabelStyle(generatefont(), Color.WHITE));
        Slider msSlider = new Slider(0,100,1,false,skin);
        msSlider.setValue(prefs.getFloat("Music",100));
        msSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MyClass.sd.VolumeMusic(msSlider.getValue());
            }
        });
        sliderContainer = new Container<Slider>(msSlider);
        sliderContainer.setTransform(true);
        sliderContainer.setScale(5);
        sliderContainer.setScaleX(((table.getWidth()-100)/2)/msSlider.getWidth());
        Log.d("Slider","Width: "+((table.getWidth()-100)/2)+" SizeX: "+msSlider.getWidth()/2);

        sliderContainer.setOrigin(msSlider.getWidth()/2,msSlider.getHeight()/2);
        sliderContainer.setSize(msSlider.getWidth()/(((table.getWidth()-100)/2)/msSlider.getWidth()),msSlider.getHeight());

        Label efVolume = new Label("Effects Volume",new Label.LabelStyle(generatefont(), Color.WHITE));
        Slider efSlider = new Slider(0,100,1,false,skin);
        efSlider.setValue(prefs.getFloat("SoundEffect",100));
        efSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               MyClass.sd.LowerSoundEffects(efSlider.getValue());
            }
        });

        Container<Slider> efContainer = new Container<Slider>(efSlider);
        efContainer.setTransform(true);
        efContainer.setScale(5);
        efContainer.setScaleX(((table.getWidth()-100)/2)/msSlider.getWidth());
        Log.d("Slider","Width: "+((table.getWidth()-100)/2)+" SizeX: "+msSlider.getWidth()/2);

        efContainer.setOrigin(msSlider.getWidth()/2,msSlider.getHeight()/2);
        efContainer.setSize(msSlider.getWidth()/(((table.getWidth()-100)/2)/msSlider.getWidth()),msSlider.getHeight());


        Label notifyLb = new Label("Notifications",new Label.LabelStyle(generatefont(), Color.WHITE));

        TextButton notifyChecker = new TextButton(null,skin.get("toggle",TextButton.TextButtonStyle.class));
        notifyChecker.setSize(msSlider.getWidth()/(((table.getWidth()-100)/2)/msSlider.getWidth()),msSlider.getHeight());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
           notifyLb.setVisible(false);
           notifyChecker.setVisible(false);
        }
        else{
            NotificationManager mNotificationManager = (NotificationManager) ((AndroidApplication) Gdx.app).getContext().getSystemService(((AndroidApplication) Gdx.app).getContext().NOTIFICATION_SERVICE);
            if(!mNotificationManager.isNotificationPolicyAccessGranted()){
                notifyLb.setVisible(false);
                notifyChecker.setVisible(false);
            }
            else{
                notifyChecker.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if(isToggled){
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALARMS);
                            }
                        }
                        else{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                mNotificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
                            }
                        }
                        isToggled = !isToggled;
                    };
                });
            }





        }



        TextButton mainMenu = new TextButton("Main Menu",skin);

        mainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Intent intent=new Intent("Main Menu");
                ((AndroidApplication) Gdx.app).getContext().sendBroadcast(intent);
            };
        });

        mainMenu.setSize(100,100);



        actors.add(msVolume);
        actors.add(sliderContainer);
        actors.add(efVolume);
        actors.add(efContainer);
        actors.add(notifyLb);
        actors.add(notifyChecker);
        actors.add(mainMenu);
        actors.add(efSlider);

        return actors;
    }
    Container<Slider> sliderContainer;

    public void MakeMenu(ArrayList<Actor> actors,Stage stage){
        for (int i=0;i<3;i++){
            table.row().pad(0,50,0,50);
            table.add(actors.get(i*2)).left().pad(0,50,0,0);

            if(i*2+1 == 5){
                table.add(actors.get((i*2)+1)).fill().pad(0,0,0,50);
            }
            else{
                table.add(actors.get((i*2)+1)).width(sliderContainer.getWidth()).pad(0,0,0,50);
            }
        }
        table.row().pad(10,500,0,500);
        table.add(actors.get(6)).expand().fill().colspan(2);
    }





    public BitmapFont generatefont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/gungeon.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 50;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.minFilter = Texture.TextureFilter.Linear;
        BitmapFont font32 = generator.generateFont(parameter); // font size 32 pixels
        font32.getData().setScale(2f);
        generator.dispose();
        return font32;
    }
}
