package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final String MEALS_JSP = "/meals.jsp";
    private static final String MEAL_JSP = "/meal.jsp";
    private static final Logger log = getLogger(MealServlet.class);
    private static final MealDao dao = new MealDaoInMemory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("forward to meals");
        String forward = MEAL_JSP;
        String action = req.getParameter("action");
        boolean isRedirect = false;
        if (action == null) {
            forward = MEALS_JSP;
            req.setAttribute("mealTos", dao.getAll());
        } else if (action.equalsIgnoreCase("delete") ||
                action.equalsIgnoreCase("edit")) {
            int id = Integer.parseInt(req.getParameter("id"));

            if (action.equalsIgnoreCase("delete")) {
                dao.delete(id);
                isRedirect = true;
            } else {
                req.setAttribute("meal", dao.getById(id));
            }
        }
        if (isRedirect) {
            resp.sendRedirect(req.getContextPath() + "/meals");
        } else {
            req.getRequestDispatcher(forward).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("datetime-local"));

        String id = req.getParameter("id");
        if (id == null || id.isEmpty()) {
            Meal meal = new Meal(dao.getCounter(), dateTime, description, calories);
            dao.add(meal);
        } else {
            Meal meal = new Meal(Integer.parseInt(id), dateTime, description, calories);
            dao.update(meal);
        }
        resp.sendRedirect(req.getContextPath() + "/meals");
    }
}
