package com.mikhail.tarasevich.university;

import com.mikhail.tarasevich.university.config.SpringConfig;
import com.mikhail.tarasevich.university.controller.FrontController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(
                SpringConfig.class
        );

        FrontController frontController = applicationContext.getBean(
                "frontController", FrontController.class
        );

        frontController.startMenu(4);
    }

}
