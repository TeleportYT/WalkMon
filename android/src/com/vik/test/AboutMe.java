package com.vik.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

public class AboutMe extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((TextView)findViewById(R.id.textView4)).setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }


        TextView tx = findViewById(R.id.textView5);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            tx.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
        RadioGroup rb = findViewById(R.id.toggle);

        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.your){
                    tx.setText("Id: 215360835\nProgrammer Name: Vivien Evgueniev\nSchool: Mekif Gimel HeAmit\nTeacher's name: Eli Siniaski\nDate: 20/03/2023");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tx.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                    }
                    tx.setGravity(Gravity.CENTER);
                }
                else{
                    tx.setText("Welcome to Caves of chaos\nthis game is all about cleaning the room you are in, you have 3 controllers\nJoyStick: when you move the jostick up you move forward, if you move it down you move backwards and etc.\nButton:if you press it you shoot bullets\nFinger on screen: when you move your finger across the screen it will move the player camera\nYour goal is to kill everyone in the room you are playing");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        tx.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                    }
                    tx.setGravity(Gravity.LEFT);
                }
            }
        });






        ((Button)findViewById(R.id.button8)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nt = new Intent(getApplicationContext(),AndroidLauncher.class);
                finish();
                startActivity(nt);
            }
        });


    }

}
