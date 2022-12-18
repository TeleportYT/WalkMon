package com.vik.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BulletManager {
    public static List<Bullet> bullets;
    private Iterator<Bullet> checker;

    public BulletManager(){
        bullets = new ArrayList<Bullet>();
    }

    public void AddBullet(Bullet bullet){
        bullets.add(bullet);
    }

    public void Update(){
        checker = bullets.iterator();
        while(checker.hasNext()){
            Bullet bt = checker.next();
            bt.Update();
            if(!bullets.contains(bt)){
                break;
            }
        }
    }

}
