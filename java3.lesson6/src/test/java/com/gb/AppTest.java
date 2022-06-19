package com.gb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AppTest {
    private App app = new App();

    @Test
    public void testAddForPositiveMass(){
        int[] test = app.sortMass(new int[]{1,6,6,4,2,3,4,6,7,8});
        Assertions.assertArrayEquals(new int []{6,7,8}, test);
    }

    @Test
    public void testAddForNegativeMass(){
        int[] test = app.sortMass(new int[]{5,6,6,3,2,3,5,6,7,8});
        Assertions.assertArrayEquals(null , test);
    }

    @Test
    public void testAddForEmptyMass(){
        int[] test = app.sortMass(new int[]{});
        Assertions.assertArrayEquals(null , test);
    }
    @Test
    public void testAddForPositiveAllFoursMass(){
        int[] test = app.sortMass(new int[]{4,4,4,4,4,4,4,4,4,4});
        Assertions.assertArrayEquals(new int[]{}, test);
    }

    @Test
    public void testCheckPoistivecheckMass(){
        boolean[] test = new boolean[] {app.checkMass(new int[]{1,6,6,4,2,3,4,6,7,8})};
        Assertions.assertArrayEquals(new boolean[]{true}, test);
    }

    @Test
    public void testCheckNegativeCheckMass(){
        boolean[] test = new boolean[] {app.checkMass(new int[]{2,6,6,5,2,3,8,6,7,8})};
        Assertions.assertArrayEquals(new boolean[]{false}, test);
    }

    @Test
    public void testCheckNegativeEmptyCheckMass(){
        boolean[] test = new boolean[] {app.checkMass(new int[]{})};
        Assertions.assertArrayEquals(new boolean[]{false}, test);
    }


}