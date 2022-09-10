package com.vik.test;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int[][] mapArr;
    private List<Wall> wallsList;

    public int getSize() {
        return Size;
    }

    private int Size;
    private int maxLength;
    private int maxTunnels;
    private World world;
    public int startX,startY;

    public Level(int Size, int maxLength, int maxTunnels,World world){
        this.Size = Size;
        this.maxLength = maxLength;
        this.maxTunnels = maxTunnels;
        this.world = world;
        mapArr = new int[this.Size][this.Size];
        this.wallsList = new ArrayList<>();
        int currentRow = (int)Math.floor(Math.random()*this.Size);
        int currentColumn = (int)Math.floor(Math.random()*this.Size);
        this.startX = currentRow;
        this.startY = currentColumn;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[] nextDirection = new int[2];
        int[] lastDirection = new int[2];

        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();
        Material material = new Material(ColorAttribute.createDiffuse(Color.GRAY));
        MeshPartBuilder builder = modelBuilder.part("testplane", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal, material);
        builder.rect(0, 0, Size,
                Size, 0, Size,
                Size, 0, 0,
                0, 0, 0,
                0, 2, 0);
        builder.setVertexTransform(new Matrix4().rotate(new Vector3(1,0,1),90));
        Model model = modelBuilder.end();
        ModelInstance modelInstance = new ModelInstance(model);
        MyClass.instances.add(modelInstance);

        while (this.maxTunnels>0 && this.Size>0 && this.maxLength>0) {
            do {
                nextDirection = directions[(int)Math.floor(Math.random() * directions.length)];
            } while ((nextDirection[0] == -lastDirection[0] && nextDirection[1] == -lastDirection[1]) || (nextDirection[0] == lastDirection[0] && nextDirection[1] == lastDirection[1]));
            int randomLength = (int)Math.ceil(Math.random() * maxLength);
            int tunnelLength = 0;

            while (tunnelLength < randomLength) {
                if (((currentRow == 0) && (nextDirection[0] == -1)) ||
                        ((currentColumn == 0) && (nextDirection[1] == -1)) ||
                        ((currentRow == this.Size - 1) && (nextDirection[0] == 1)) ||
                        ((currentColumn == this.Size - 1) && (nextDirection[1] == 1))) {
                    break;
                } else {
                    mapArr[currentRow][currentColumn] = 1; //set the value of the index in map to 0 (a tunnel, making it one longer)
                    currentRow += nextDirection[0]; //add the value from randomDirection to row and col (-1, 0, or 1) to update our location
                    currentColumn += nextDirection[1];
                    tunnelLength++; //the tunnel is now one longer, so lets increment that variable
                }

            }
            lastDirection = nextDirection; //set lastDirection, so we can remember what way we went
            this.maxTunnels--;



        }


        for(int i = 0; i<this.Size;i++){
            for(int j = 0; j<this.Size;j++){
                if(mapArr[i][j] == 0){
                    wallsList.add(new Wall(i,j,this.world));
                }
            }
        }
    }

    public int getCollision(int x, int y){
        if(x<0 || y<0){
            return 0;
        }
        if(x>=this.Size || y>=this.Size){
            return 0;
        }
        return mapArr[x][y];
    }



    public boolean lineOfSightCheap(Vector3 pos1, Vector3 pos2){

        Vector3 tmp = new Vector3();
        tmp.set(pos1);

        Vector3 dir = new Vector3();
        dir.set(tmp).sub(pos2);
        dir.y = 0;
        dir.nor();

        while(tmp.dst2(pos2) > (0.25f) * (0.25f)){
            tmp.mulAdd(dir, -0.25f);

            if(getCollision((int)tmp.x, (int)tmp.z)!=1)
                return false;

        }


        return true;
    }

    public List<Wall> getWalls(){
        return this.wallsList;
    }


}
