package com.vik.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutMe extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_me);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((TextView)findViewById(R.id.textView4)).setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }


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
