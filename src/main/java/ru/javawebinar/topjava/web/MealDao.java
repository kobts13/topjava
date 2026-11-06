package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public interface MealDao {
    void delete(int id);

    List<MealTo> getAll();

    Meal getById(int id);

    void add(Meal meal);

    void update(Meal meal);

    Integer getCounter();
}
