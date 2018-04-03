package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

public interface MealService {
    Meal save(int userId, Meal meal);

    void delete(int userId,int id) throws NotFoundException;

    Meal get(int userId,int id) throws NotFoundException;

    List<Meal> getAll(int userId);
}