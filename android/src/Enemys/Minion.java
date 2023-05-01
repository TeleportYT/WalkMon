package Enemys;

import static com.vik.test.Game.mg;

import com.vik.test.Game;

public class Minion extends Warrior{
    private Duplicator father;



    public Minion(float x, float z,Duplicator father) {
        super(x, z);
        this.father = father;
    }


    @Override
    public void Die(){
        this.father.getMinions().remove(this);
        mg.getSceneManager().removeScene(this.enemyModel);
    }
}
