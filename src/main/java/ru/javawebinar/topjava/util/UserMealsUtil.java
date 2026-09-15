package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        List<UserMealWithExcess> mealsTo2 = filteredByCycles2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo2.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println(filteredByStreams2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesPerDay = new HashMap<>();
        meals.forEach(userMeal ->
                totalCaloriesPerDay.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum));
        List<UserMealWithExcess> filtered = new ArrayList<>();
        meals.forEach(userMeal -> {
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean excess = caloriesPerDay < totalCaloriesPerDay.get(userMeal.getDateTime().toLocalDate());
                UserMealWithExcess userMealWithExcess = new UserMealWithExcess(
                        userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        excess);
                filtered.add(userMealWithExcess);
            }
        });
        return filtered;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesPerDay = meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().toLocalDate(), Collectors.summingInt(UserMeal::getCalories)));
        return meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime))
                .map(userMeal -> {
                    boolean excess = caloriesPerDay < totalCaloriesPerDay.get(userMeal.getDateTime().toLocalDate());
                    return new UserMealWithExcess(
                            userMeal.getDateTime(),
                            userMeal.getDescription(),
                            userMeal.getCalories(),
                            excess);
                })
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExcess> filteredByCycles2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> totalCaloriesPerDay = new HashMap<>();
        List<UserMeal> filtered = new ArrayList<>();
        meals.forEach(userMeal -> {
            totalCaloriesPerDay.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                filtered.add(userMeal);
            }
        });
        return new AbstractList<UserMealWithExcess>() {
            @Override
            public UserMealWithExcess get(int index) {
                UserMeal userMeal = filtered.get(index);
                boolean excess = caloriesPerDay < totalCaloriesPerDay.get(userMeal.getDateTime().toLocalDate());
                return new UserMealWithExcess(
                        userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        excess
                );
            }

            @Override
            public int size() {
                return filtered.size();
            }
        };
    }

    public static List<UserMealWithExcess> filteredByStreams2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return meals.stream()
                .collect(Collector.of(
                        () -> new Object() {
                            final List<UserMeal> filtered = new ArrayList<>();
                            final Map<LocalDate, Integer> totalCaloriesPerDay = new HashMap<>();
                        },
                        (accumulator, userMeal) -> {
                            accumulator.totalCaloriesPerDay.merge(userMeal.getDateTime().toLocalDate(), userMeal.getCalories(), Integer::sum);
                            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                                accumulator.filtered.add(userMeal);
                            }
                        }, (accumulator1, accumulator2) -> {
                            accumulator1.filtered.addAll(accumulator2.filtered);
                            accumulator2.totalCaloriesPerDay.forEach((date, calories) ->
                                    accumulator1.totalCaloriesPerDay.merge(date, calories, Integer::sum)
                            );
                            return accumulator1;
                        },
                        accumulator -> accumulator.filtered.stream()
                                .map(userMeal -> {
                                    boolean excess = caloriesPerDay < accumulator.totalCaloriesPerDay.get(userMeal.getDateTime().toLocalDate());
                                    return new UserMealWithExcess(
                                            userMeal.getDateTime(),
                                            userMeal.getDescription(),
                                            userMeal.getCalories(),
                                            excess);
                                }).collect(Collectors.toList())));
    }
}
