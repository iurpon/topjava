package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collection;
import java.util.List;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal save(int userId, Meal meal) {
        return repository.save(userId,meal);
    }

    @Override
    public void delete(int userId, int id) throws NotFoundException{
        ValidationUtil.checkNotFound(repository.delete(userId,id) ,
                String.format("no meal with id %d and userId %d",id,userId));
    }

    @Override
    public Meal get(int userId, int id) throws NotFoundException {
        return ValidationUtil.checkNotFound(repository.get(userId,id),
                String.format("no meal with id %d and userId %d",id,userId));
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }
}