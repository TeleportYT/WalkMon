package com.vik.test;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AllStats extends Activity {

    ListView lt;
    GameStatsDbHelper dbHelper;
    List<GameStats> Allstats;
    StatsAdpter Useradapter;
    private FirebaseAuth mAuth;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_stats);


        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential);


        dbHelper = new GameStatsDbHelper(getApplicationContext());


        Allstats = dbHelper.getAllGameStats();
        List<GameStats> Allstats = dbHelper.getAllGameStats();
        Collections.reverse(Allstats);
        Useradapter = new StatsAdpter(this, Allstats);
        lt = findViewById(R.id.lt);

        lt.setAdapter(Useradapter);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ((TextView)findViewById(R.id.textView3)).setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }


        ((Button)findViewById(R.id.button7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent nt = new Intent(getApplicationContext(),AndroidLauncher.class);
                startActivity(nt);
            }
        });


        RadioGroup rb = findViewById(R.id.toggle);

        rb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.your){
                    lt.setAdapter(Useradapter);
                }
                else{
                    ShowLeaderboard();
                }
            }
        });






    }



    public void ShowLeaderboard(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference leaderboardRef = database.getReference("leaderboard");
        List<GameStats> LeaderBoard = new ArrayList<>();
        List<String> usernames = new ArrayList<>();
        Map<String,GameStats> map = new HashMap<>();
// Attach a listener to the "leaderboard" node to retrieve all the GameStats objects
        leaderboardRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    String username = userSnapshot.child("username").getValue(String.class);
                    GameStats gameStats = userSnapshot.child("GameStat").getValue(GameStats.class);
                    if (gameStats != null) {
                        usernames.add(username);
                        LeaderBoard.add(gameStats);
                        map.put(username, gameStats);
                    }
                }



                Map<String,GameStats> sortedMap = SortMap(map);
                LeaderboardAdapter adapter = new LeaderboardAdapter(getApplicationContext(), ReturnSortedStats(sortedMap),ReturnSortedUsernames(sortedMap));
                lt.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur while retrieving the leaderboard data
                Log.w("Error", "Failed to read leaderboard data.", error.toException());
            }
        });


    }

    public Map<String,GameStats> SortMap(Map<String,GameStats> map){
        LinkedHashMap<String,GameStats> sortedMap = new LinkedHashMap<String,GameStats>();
        ArrayList<GameStats> list = new ArrayList<>();
        for (Map.Entry<String, GameStats> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list, new Comparator<GameStats>() {
            @Override
            public int compare(GameStats gameStats, GameStats t1) {
                if(gameStats.getPlayerScore() > t1.getPlayerScore()){
                    return -1;
                }
                else if(gameStats.getPlayerScore() < t1.getPlayerScore()){
                    return 1;
                }
                return 0;
            }
        });

        for (GameStats str : list) {
            for (Map.Entry<String, GameStats> entry : map.entrySet()) {
                if (entry.getValue().equals(str)) {
                    sortedMap.put(entry.getKey(), str);
                }
            }
        }

        return sortedMap;
    }


    public List<GameStats> ReturnSortedStats(Map<String,GameStats> map){
        List<GameStats> list = new ArrayList<>();
        for (Map.Entry<String, GameStats> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        return list;
    }

    public List<String> ReturnSortedUsernames(Map<String,GameStats> map){
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, GameStats> entry : map.entrySet()) {
            list.add(entry.getKey());
        }
        return list;
    }
}
