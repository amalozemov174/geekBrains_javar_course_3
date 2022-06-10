package com.gb;

public class App {
    private final Object mon = new Object();
    private final Object mon2 = new Object();
    private volatile char currentLetter = 'A';



    public static void main(String[] args) throws InterruptedException {
        App testApp= new App();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                testApp.printA();
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                testApp.printB();
            }
        });

        Thread c = new Thread(new Runnable() {
            @Override
            public void run() {
                testApp.printC();
            }
        });

        a.start();
        b.start();
        c.start();
    }

    public void printA(){
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'A') {
                        mon.wait();
                    }
                    System.out.print("A");
                    currentLetter = 'B';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void printB(){
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'B') {
                        mon.wait();
                    }
                    System.out.print("B");
                    currentLetter = 'C';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public void printC(){
        synchronized (mon) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'C') {
                        mon.wait();
                    }
                    System.out.print("C");
                    currentLetter = 'A';
                    mon.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
