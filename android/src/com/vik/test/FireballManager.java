package com.vik.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FireballManager {

    public static List<FireBall> fireballs;
    private Iterator<FireBall> checker;

    public FireballManager(){
        fireballs = new ArrayList<FireBall>();
    }

    public void AddFireBall(FireBall fireball){
        fireballs.add(fireball);
    }

    public void Update(){
        checker = fireballs.iterator();
        while(checker.hasNext()){
            FireBall fb = checker.next();
            fb.Update();
            if(!fireballs.contains(fb)){
                break;
            }
        }
    }

}
