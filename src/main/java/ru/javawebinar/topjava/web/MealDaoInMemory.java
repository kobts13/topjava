package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoInMemory implements MealDao {
    private static final AtomicInteger ATOMIC_INTEGER = new AtomicInteger(10);
    private final List<Meal> meals = Collections.synchronizedList(new ArrayList<>(MealsUtil.meals));

    @Override
    public void delete(int id) {
        meals.removeIf(meal -> meal.getId().equals(id));
    }

    @Override
    public List<MealTo> getAll() {
        return MealsUtil.convertTo(meals);
    }

    @Override
    public Meal getById(int id) {
        return meals.stream()
                .filter(meal -> meal.getId().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public void add(Meal meal) {
        meals.add(meal);
    }

    @Override
    public void update(Meal meal) {
        delete(meal.getId());
        add(meal);
    }

    @Override
    public Integer getCounter() {
        return ATOMIC_INTEGER.incrementAndGet();
    }
}
