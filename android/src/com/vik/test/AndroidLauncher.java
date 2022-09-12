package com.vik.test;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);

		Button bt = (Button) findViewById(R.id.Bitch);
		bt.setOnClickListener(this::OnClick);


		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		ConstraintLayout l = (ConstraintLayout) findViewById(R.id.ct);
		l.addView(initializeForView(new BgMoving(), config));

	}


	public void OnClick (View v) {
		ConstraintLayout l = (ConstraintLayout) findViewById(R.id.ct);
		l.removeAllViews();
		finish();
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
	}
}
