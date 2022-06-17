package com.gb;

public class TestClass {

    @BeforeSuite
    public void Before(){
        System.out.println("Выполняю в начале");
    }

//    @BeforeSuite
//    public void BeforeException(){
//
//    }

    @Test (priority = 1)
    public void Test1(){
        System.out.println("Test1 : Приоритет 1");
    }

    @Test (priority = 1)
    public void Test2(){
        System.out.println("Test2 : Приоритет 1");
    }

    @Test (priority = 2)
    public void Test3(){
        System.out.println("Test3 : Приоритет 2");
    }

    @Test (priority = 5)
    public void Test4(){
        System.out.println("Test4 : Приоритет 5");
    }

    @Test (priority = 3)
    public void Test5(){
        System.out.println("Test5 : Приоритет 3");
    }

    @Test (priority = 2)
    public void Test6(){
        System.out.println("Test6 : Приоритет 2");
    }

    @AfterSuite
    public void After(){
        System.out.println("Выполняю в конце");
    }

//    @AfterSuite
//    public void AfterException(){
//
//    }
}
