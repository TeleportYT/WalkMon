package com.vik.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jdk.internal.org.jline.utils.Log;

public class Level {
    private int[][] mapArr;
    private List<Wall> wallsList;
    private int Size;
    private int maxLength;
    private int maxTunnels;
    private int startX,startY;

    public Level(int Size, int maxLength, int maxTunnels){
        this.Size = Size;
        this.maxLength = maxLength;
        this.maxTunnels = maxTunnels;
        mapArr = new int[this.Size][this.Size];
        this.wallsList = new ArrayList<>();
        int currentRow = (int)Math.floor(Math.random()*this.Size);
        int currentColumn = (int)Math.floor(Math.random()*this.Size);
        this.startX = currentRow;
        this.startY = currentColumn;
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        int[] nextDirection = new int[2];
        int[] lastDirection = new int[2];

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
                    wallsList.add(new Wall(i,j));
                }
            }
        }
    }

    public List<Wall> getWalls(){
        return this.wallsList;
    }


}
