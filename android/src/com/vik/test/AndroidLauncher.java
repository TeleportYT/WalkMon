package com.vik.test;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AndroidLauncher extends AndroidApplication {

	private static final int RC_SIGN_IN = 9001;
	private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
	private TextView username,gameName;
	private ImageView pfp;
	public ArrayList<View> views;
	private Button play,settings,about,exit;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		views = new ArrayList<View>();
		mAuth = FirebaseAuth.getInstance();
		LoadViews();
	}

	public void LoadViews(){
		play = (Button) findViewById(R.id.Bitch);
		play.setOnClickListener(this::OnClick);
		views.add(play);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		ConstraintLayout l = (ConstraintLayout) findViewById(R.id.ct);
		View v = initializeForView(new BgMoving(), config);
		l.addView(v);
		views.add(v);



		LinearLayout lt = (LinearLayout)findViewById(R.id.lt);

		username = findViewById(R.id.username);
		pfp = findViewById(R.id.pfp);

		views.add(username);
		views.add(pfp);


		gameName = findViewById(R.id.textView2);
		views.add(gameName);



		settings = findViewById(R.id.button);
		about = findViewById(R.id.button2);
		exit = findViewById(R.id.button3);


		about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent nt = new Intent(getApplicationContext(),AboutMe.class);
				finish();
				startActivity(nt);
			}
		});




		exit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ShowExit();
			}
		});


		setUpMusic();

		NotificationManager n = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if(!n.isNotificationPolicyAccessGranted()){
				Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
				startActivityForResult(intent,69);
			}
		}

	}

	private void requestPermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NOTIFICATION_POLICY}, 79);
		} else {
			ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_NOTIFICATION_POLICY},
					79);
		}
		if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY)) {
		} else {
			ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_NOTIFICATION_POLICY},
					79);
		}
	}



	public void OnClick(View v) {
		Intent nt = new Intent(getApplicationContext(),GameLayout.class);
		finish();
		startActivity(nt);
	}




	//region exit
	@Override
	public void onBackPressed() {
		ShowExit();
	}



	public void ShowExit(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name);
		builder.setIcon(R.drawable.ic_baseline_exit_to_app_24);
		builder.setMessage("Do you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();

	}

	//endregion

	//region firebase

	@Override
	protected void onStart() {
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);

		super.onStart();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			// The Task returned from this call is always completed, no need to attach
			// a listener.
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				// Google Sign In was successful, authenticate with Firebase
				GoogleSignInAccount account = task.getResult(ApiException.class);
				firebaseAuthWithGoogle(account);
			} catch (ApiException e) {
				// Google Sign In failed, update UI appropriately
				Log.w("Failed", "Google sign in failed", e);
				mGoogleSignInClient.signOut();
				Intent signInIntent = mGoogleSignInClient.getSignInIntent();
				startActivityForResult(signInIntent, RC_SIGN_IN);
				// ...
			}
		}
	}

	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		mAuth.signInWithCredential(credential)
				.addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							// Sign in success, update UI with the signed-in user's information
							Log.d(TAG, "signInWithCredential:success");
							FirebaseUser user = mAuth.getCurrentUser();
							UpdateUI(user);
							// ...
						} else {
							// If sign in fails, display a message to the user.
							Log.w(TAG, "signInWithCredential:failure", task.getException());
							Toast.makeText(getApplicationContext(), "Authentication failed.",
									Toast.LENGTH_SHORT).show();
						}

					}
				});
	}
	User player;
	public void UpdateUI(FirebaseUser user){
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myRef = database.getReference("users");

		myRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot snapshot) {
				if (snapshot.getValue() == null) {
					player = new User(user.getDisplayName(),0,0,user.getPhotoUrl().toString());
					myRef.child(user.getUid()).setValue(player).addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							Toast.makeText(getApplicationContext(),"Error "+e,Toast.LENGTH_SHORT).show();
						}
					});
				}
				else{
					player = snapshot.getValue(User.class);
				}
				username.setText(player.getUsername());
				Uri uri = Uri.parse(player.getPfpUri());
				Picasso.get().load(user.getPhotoUrl()).into(pfp);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error "+error,Toast.LENGTH_SHORT).show();
			}
		});

		requestPermission();
	}

	//endregion


	//region musicService
	musicService musicS;
	boolean isBound = false;


	public void setUpMusic(){
		Intent music = new Intent(this,musicService.class);
		bindService(music,connection, Context.BIND_AUTO_CREATE);
		startService(music);

	}


	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className,
									   IBinder service) {
			// We've bound to LocalService, cast the IBinder and get LocalService instance.
			musicService.LocalBinder binder = (musicService.LocalBinder) service;
			musicS = binder.getService();
			isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			isBound = false;
		}
	};

	//endregion

	@Override
	protected void onPause() {
		super.onPause();
		if (isBound){
			musicS.pauseMusic();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if	(isBound){
			musicS.ResumeMusic();
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		musicS.onDestroy();
	}
}
