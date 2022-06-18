package com.gb;

import java.util.Arrays;

public class App {

    public static void main (String[] args) throws RuntimeException{

        App test = new App();
        //System.out.println(test.sortMass(new int[]{ 4,4,4,4,4,4,4,4,4,5 }));

        //int i = 1;
        System.out.println(test.checkMass(new int[]{ 1,6,6,4,2,3,4,6,7,8 }));
        //int[] test1 = test.sortMass(new int[]{1,6,6,4,2,3,4,6,7,8});


    }

    public int[] sortMass(int[] mass){
        int id = 0;
        for(int i = 0; i < mass.length ; i++){
            if(mass[i] == 4){
               id = i;
            }
        }
        if(id == 0){
            throw new RuntimeException("Массив не содержит цифру 4");
        }

        int[] res = Arrays.copyOfRange(mass, id + 1, mass.length);

        return res;
    }

    public boolean checkMass(int[] mass){
        boolean res = false;

        for(int i : mass){
            if (i == 1 || i == 4){
                res = true;
            }
        }

        return res;
    }

}
