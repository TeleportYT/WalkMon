package com.vik.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BulletManager {
    public static List<Bullet> bullets;
    private Iterator<Bullet> checker;

    public BulletManager(){
        this.bullets = new ArrayList<Bullet>();
    }

    public void AddBullet(Bullet bullet){
        this.bullets.add(bullet);
    }

    public void Update(){
        this.checker = this.bullets.iterator();
        while(this.checker.hasNext()){
            Bullet bt = this.checker.next();
            bt.Update();
            if(!this.bullets.contains(bt)){
                break;
            }
        }
    }

}
