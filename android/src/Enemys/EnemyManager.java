package Enemys;

import static com.vik.test.MyClass.mapLevel;

import com.vik.test.FireballManager;
import com.vik.test.MyClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemyManager {

    public static List<Enemy> enemyies;
    public static FireballManager fbManager;
    public static List<Duplicator> Duplicators;

    public EnemyManager(){
        enemyies = new ArrayList<Enemy>();
        fbManager = new FireballManager();
        Duplicators = new ArrayList<Duplicator>();
        GenerateEnemys(2,4,1);
    }

    public void GenerateEnemys(int warriors,int bobs,int duplicators){
        int maxEnemies = warriors+bobs+duplicators;

        Random rand = new Random();

        while(maxEnemies!=0){
            if(warriors!=0){
                int x = (rand.nextInt(mapLevel.getSize()));
                int z = (rand.nextInt(mapLevel.getSize()));
                while ((MyClass.mapLevel.getCollision(x,z) != 1)){
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
                while ((MyClass.mapLevel.getCollision(x,z) != 1)){
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
                while ((MyClass.mapLevel.getCollision(x,z) != 1)){
                    x = (rand.nextInt(mapLevel.getSize()));
                    z = (rand.nextInt(mapLevel.getSize()));
                }
                Duplicator dp = new Duplicator(x,z);
                Duplicators.add(dp);
                duplicators--;
                maxEnemies--;
            }
        }
    }






    public void Update(){
        fbManager.Update();
            for (Enemy enemy : enemyies) {
                enemy.Update();
            }
            for (Duplicator enemy : Duplicators){
                enemy.Update();
            }
    }

    public static void AddEnemy(Enemy enemy){
        enemyies.add(enemy);
    }
    public static void RemoveEnemy(Enemy enemy){
        if(enemy instanceof Duplicator){
            Duplicators.remove(enemy);
        }
        else{
            enemyies.remove(enemy);
        }
            MyClass.instances.remove(enemy.getModelInstance());
    }



}
