package Enemys;

import static com.vik.test.Game.mapLevel;
import static com.vik.test.Game.stats;

import android.content.Intent;
import android.util.Log;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.vik.test.Difficulty;
import com.vik.test.FireballManager;
import com.vik.test.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyManager {

    public static List<Enemy> enemyies;
    public static FireballManager fbManager;
    public static List<Duplicator> Duplicators;


    static final float DuplicatorPoints=10f;
    static float bobPoints = 5f;
    static float warriorPoints = 2f;

    private int enemiesAmount;

    public EnemyManager(Difficulty difficulty){
        this.enemyies = new ArrayList<Enemy>();
        this.fbManager = new FireballManager();
        this.Duplicators = new ArrayList<Duplicator>();
        GenerateByDifficulty(difficulty );
    }

    public void GenerateByDifficulty(Difficulty difficulty){
        int warriors=0,bobs=0,duplicators=0;
        switch (difficulty){
            case Easy:
                warriors = 4+ new Random().nextInt(5);
                bobs = new Random().nextInt(5);
                break;
            case Medium:
                warriors = 10+ new Random().nextInt(5);
                bobs = 5+new Random().nextInt(5);
                duplicators = new Random().nextInt(2);
                break;
            case Hard:
                warriors = 10+ new Random().nextInt(7);
                bobs = 5+new Random().nextInt(5);
                duplicators = 2+new Random().nextInt(4);
            case Testing:
                break;
        }
        GenerateEnemys(warriors,bobs,duplicators);

    }



    public void GenerateEnemys(int warriors,int bobs,int duplicators){
        int maxEnemies = warriors+bobs+duplicators;
        this.enemiesAmount = maxEnemies;
        Log.d("AmountS","We have: "+this.enemiesAmount);
        Random rand = new Random();

        while(maxEnemies!=0){
            if(warriors!=0){
                int x = (rand.nextInt(mapLevel.getSize()));
                int z = (rand.nextInt(mapLevel.getSize()));
                while ((Game.mapLevel.getCollision(x,z) != 1)){
                    x = (rand.nextInt(mapLevel.getSize()));
                    z = (rand.nextInt(mapLevel.getSize()));
                }
                AddEnemy(new Warrior(x,z));
                warriors--;
                maxEnemies--;
            }
            if(bobs!=0){
                int x = (rand.nextInt(mapLevel.getSize()));
                int z = (rand.nextInt(mapLevel.getSize()));
                while ((Game.mapLevel.getCollision(x,z) != 1)){
                    x = (rand.nextInt(mapLevel.getSize()));
                    z = (rand.nextInt(mapLevel.getSize()));
                }
                AddEnemy(new Bob(x,z));
                bobs--;
                maxEnemies--;
            }
            if(duplicators!=0){
                int x = (rand.nextInt(mapLevel.getSize()));
                int z = (rand.nextInt(mapLevel.getSize()));
                while ((Game.mapLevel.getCollision(x,z) != 1)){
                    x = (rand.nextInt(mapLevel.getSize()));
                    z = (rand.nextInt(mapLevel.getSize()));
                }
                Duplicator dp = new Duplicator(x,z);
                this.Duplicators.add(dp);
                duplicators--;
                maxEnemies--;
            }
        }
    }




    boolean isFinished = false;
    public void Update(){
        this.fbManager.Update();
        for (Enemy enemy : this.enemyies) {
            enemy.Update();
        }
        for (Duplicator enemy : this.Duplicators){
            enemy.Update();
        }
        Log.d("AmountD","Left: "+enemyies.size()+" "+this.Duplicators.size());
        if (this.enemyies.isEmpty() && this.Duplicators.isEmpty() && !this.isFinished){
            Log.d("Winned","All enemies cleared");
            Intent nt = new Intent("Stage Cleared");
            ((AndroidApplication) Gdx.app).getContext().sendBroadcast(nt);
            this.isFinished = true;

        }

    }

    public static void AddEnemy(Enemy enemy){
        enemyies.add(enemy);
    }

    public static void RemoveEnemy(Enemy enemy){

        stats.incrementEnemiesKilled();
        if(enemy instanceof Duplicator){
            stats.addPlayerScore(DuplicatorPoints);
            Duplicators.remove(enemy);
        }
        else{
            if(enemy instanceof Bob){
                stats.addPlayerScore(bobPoints);
            }
            else{
                stats.addPlayerScore(warriorPoints);
            }
            enemyies.remove(enemy);
        }
    }



}
