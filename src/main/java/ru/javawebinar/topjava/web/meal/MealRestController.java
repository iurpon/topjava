package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;

    public Meal save( int userId,Meal meal){
        return service.save(userId,meal);
    }

    public void delete(int userId,int id) throws NotFoundException{
        service.delete(userId,id);
    }

    public Meal get(int userId,int id) throws NotFoundException{
        return service.get(userId,id);
    }

    public List<Meal> getAll(int userId){
        return service.getAll(userId);
    }

}