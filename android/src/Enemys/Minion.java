package Enemys;

import com.vik.test.MyClass;

public class Minion extends Warrior{
    private Duplicator father;



    public Minion(float x, float z,Duplicator father) {
        super(x, z);
        this.father = father;
    }


    @Override
    public void Die(){
        father.getMinions().remove(this);
        MyClass.instances.remove(this.modelInstance);
    }
}
