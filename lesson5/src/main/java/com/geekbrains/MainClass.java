package com.geekbrains;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class MainClass {
    public static final int CARS_COUNT = 4;

    public static void main(String[] args) {

        Semaphore smp = new Semaphore(1);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(CARS_COUNT);
        final CountDownLatch cdl = new CountDownLatch(CARS_COUNT);
        Race race = new Race(new Road(60), new Tunnel(CARS_COUNT), new Road(40));
        Car[] cars = new Car[CARS_COUNT];

        new Thread (() ->{
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Подготовка!!!");

            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка началась!!!");

            int tmpCount = 0;
            while(true){
                for (Car c : cars) {
                    if (c.isFinished()){
                        tmpCount++;
                    }
                }
                if(tmpCount++ == CARS_COUNT){
                    System.out.println("ВАЖНОЕ ОБЪЯВЛЕНИЕ >>> Гонка закончилась!!!");
                    break;
                }
                tmpCount = 0;
            }
        }).start();

        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(race, 20 + (int) (Math.random() * 10), cyclicBarrier, cdl);
        }
        for (int i = 0; i < cars.length; i++) {
            new Thread(cars[i]).start();
        }

    }
}
