package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer,Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(m -> save(m.getUserId(),m));

    }

    @Override
    public Meal save(int userId, Meal meal) {

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());

            repository.computeIfAbsent(userId, HashMap::new);
        }
        int id = meal.getId();
        repository.computeIfPresent(userId, (k,v) -> {v.put(id,meal); return v;});

        return meal;
    }

    @Override
    public boolean delete (int userId, int id) {
        return repository.get(userId).remove(id)!= null;
    }

    @Override
    public Meal get(int userId, int id) {
        return repository.get(userId).remove(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.get(userId).values()
                .stream().sorted(new Comparator<Meal>() {
                    @Override
                    public int compare(Meal o1, Meal o2) {
                        return o2.getDateTime().compareTo(o1.getDateTime());
                    }
                }).collect(Collectors.toList());
    }
}

