package com.vik.test;

import java.util.ArrayList;
import java.util.List;

public class FireballManager {

    public static List<FireBall> fireballs;

    public FireballManager(){
        fireballs = new ArrayList<FireBall>();
    }

    public void AddFireBall(FireBall fireball){
        fireballs.add(fireball);
    }

    public void Update(){
        for(FireBall rc : fireballs){
            rc.Update();
        }
    }

}
