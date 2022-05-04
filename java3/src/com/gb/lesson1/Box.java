package com.gb.lesson1;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private final ArrayList<T> fruitBox;
    private float weight = 0;

    public Box() {
        fruitBox = new ArrayList<T>();
    }

    public Float getWeight(){
        for (T fruit: fruitBox) {
            Number n = fruit.getWeight();
            weight += fruit.getWeight().floatValue();
        }

        return weight;
    }

    public boolean compareTo(Box box){
        if(this.getWeight().compareTo(box.getWeight()) == 0)
            return true;
        return false;
    }

    public void transferFruits(ArrayList<T> to){
        ArrayList<? extends T > from = this.getFruitBox();

        for(int i = 0; i < (from.size()); i++){
            to.add(from.get(i));
        }
        this.getFruitBox().clear();
        weight = 0f;
    }

    public ArrayList<T> getFruitBox() {
        return fruitBox;
    }

    public void addFruit(T fruit){
        fruitBox.add(fruit);
    }
}
