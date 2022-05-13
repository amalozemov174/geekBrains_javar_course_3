package com.gb.lesson1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class App {

    public static void main(String[] args){
        // 1 part
        String [] s = {"1", "2", "3","4", "5"};
        Integer [] i = {10,20,30,40,50};
        System.out.println(Arrays.asList(s));
        System.out.println(Arrays.asList(i));
        changeElements(s, 1,5);
        changeElements(i, 2,4);
        System.out.println(Arrays.asList(s));
        System.out.println(Arrays.asList(i));

        //part 2
        String [] part2 = {"Дом", "Кот", "Слон","Змея", "Пес"};
        System.out.println(convertToArrauList(i));

        //part 3
        Box<Orange> orangeBox = new Box<Orange>();
        Box<Orange> spareOrangeBox = new Box<Orange>();
        Box<Apple> appleBox = new Box<Apple>();
        Orange orange1 = new Orange();
        Orange orange2 = new Orange();
        Orange orange3 = new Orange();
        Apple apple1 = new Apple();
        Apple apple2 = new Apple();
        Apple apple3 = new Apple();

        orangeBox.addFruit(orange1);
        orangeBox.addFruit(orange2);
        //orangeBox.addFruit(orange3);
        appleBox.addFruit(apple1);
        appleBox.addFruit(apple2);
        appleBox.addFruit(apple3);

        orangeBox.compareTo(appleBox);
        System.out.println("Сравниваем объекты Box: " + orangeBox.compareTo(appleBox));
        orangeBox.transferFruits(spareOrangeBox.getFruitBox());
        System.out.println(spareOrangeBox.getFruitBox());
        System.out.println(orangeBox.getFruitBox());

        //orangeBox.transferFruits(appleBox.getFruitBox());
        //System.out.println(appleBox.getFruitBox());

    }

    public static<T> void changeElements(T[] collection, int firselement, int secondElement){
        T tmpFirst = collection[firselement - 1];
        T tmpSecond = collection[secondElement - 1];

        collection[firselement - 1] = tmpSecond;
        collection[secondElement - 1] = tmpFirst;
    }

    public static <T> ArrayList<T> convertToArrauList(T[] collection){
        ArrayList<T> tmpArray = new ArrayList<T>();

        for (T obj: collection) {
            tmpArray.add(obj);
        }

        return tmpArray;
    }

}
