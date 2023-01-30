package com.vik.test;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

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

public class AndroidLauncher extends AndroidApplication {

	private static final int RC_SIGN_IN = 9001;
	private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
	private TextView username;
	private ImageView pfp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactivity);
		mAuth = FirebaseAuth.getInstance();


		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


		Button bt = (Button) findViewById(R.id.Bitch);
		bt.setOnClickListener(this::OnClick);


		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = false;
		config.useCompass = false;

		ConstraintLayout l = (ConstraintLayout) findViewById(R.id.ct);
		l.addView(initializeForView(new BgMoving(), config));

		LinearLayout lt = (LinearLayout)findViewById(R.id.lt);

		username = findViewById(R.id.username);
		pfp = findViewById(R.id.pfp);



	}


	public void OnClick(View v) {
		ConstraintLayout l = (ConstraintLayout) findViewById(R.id.ct);
		l.removeAllViews();
		finish();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onStart() {
		/*
		mGoogleSignInClient.signOut();
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
		*/
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
					Toast.makeText(getApplicationContext(),player.getUsername(),Toast.LENGTH_SHORT).show();
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


	}


	@Override
	protected void onDestroy() {
		mAuth.signOut();
		mGoogleSignInClient.signOut();
		super.onDestroy();
	}
}
