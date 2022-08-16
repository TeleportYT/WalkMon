package com.vik.test;

import java.util.ArrayList;
import java.util.List;

public class BulletManager {
    public static List<Bullet> bullets;

    public BulletManager(){
        bullets = new ArrayList<Bullet>();
    }

    public void AddBullet(Bullet bullet){
        bullets.add(bullet);
    }

    public void Update(){
        for(Bullet rc : bullets){
            rc.Update();
        }
    }

}
