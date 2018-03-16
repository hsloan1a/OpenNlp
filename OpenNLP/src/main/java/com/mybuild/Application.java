package com.mybuild;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
    	ApplicationContext context = SpringApplication.run(Application.class, args);
    	//needs to be in root to search lower directories.....
        //System.out.println("Contains greeting  "+context.containsBeanDefinition("GreetingController"));
    }
}