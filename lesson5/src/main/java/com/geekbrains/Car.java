package com.geekbrains;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;
    private CyclicBarrier _cyclicBarrier;
    private CountDownLatch _cdl;
    private boolean _isFinished = false;
    private static boolean _isWin = false;

    public Car(Race race, int speed, CyclicBarrier cyclicBarrier) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        _cyclicBarrier = cyclicBarrier;

    }

    public Car(Race race, int speed, CyclicBarrier cyclicBarrier, CountDownLatch cdl) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
        _cyclicBarrier = cyclicBarrier;
        _cdl = cdl;
    }

    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }

    public int getCarsCount(){
        return CARS_COUNT;
    }
    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            System.out.println(this.name + " готов");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // здесь останавливаем поток

        try {
            _cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        _cdl.countDown();
        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        _isFinished = true;
        if(!_isWin){
            _isWin = true;
            System.out.println(this.getName() + " - WIN");
        }
    }

    public boolean isFinished() {
        return _isFinished;
    }
}
