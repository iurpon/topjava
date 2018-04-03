package ru.javawebinar.topjava;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.web.meal.MealRestController;
import ru.javawebinar.topjava.web.user.AdminRestController;

import java.util.Arrays;

public class SpringMain {
    public static void main(String[] args) {
        // java 7 Automatic resource management
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " );
            String[] str = appCtx.getBeanDefinitionNames();
            for(int i = 0; i < str.length; i++)
                System.out.println(str[i]);
            System.out.println();
            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.getAll().forEach(System.out::println);
            adminUserController.create(new User(null, "userName", "email", "password", Role.ROLE_ADMIN));
            adminUserController.getAll().forEach(System.out::println);
            adminUserController.delete(0);
            adminUserController.getAll().forEach(System.out::println);
//            adminUserController.delete(5);
            MealRestController mealRestController = appCtx.getBean("mealRestController",MealRestController.class);
            System.out.println("getAll() : ");
            mealRestController.getAll(0).forEach(System.out::println);

        }
    }
}
