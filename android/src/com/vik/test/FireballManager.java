package com.vik.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FireballManager {

    public static List<FireBall> fireballs;
    private Iterator<FireBall> checker;

    public FireballManager(){
        this.fireballs = new ArrayList<FireBall>();
    }

    public void AddFireBall(FireBall fireball){
        this.fireballs.add(fireball);
    }

    public void Update(){
        this.checker = this.fireballs.iterator();
        while(this.checker.hasNext()){
            FireBall fb = this.checker.next();
            fb.Update();
            if(!this.fireballs.contains(fb)){
                break;
            }
        }
    }

}
