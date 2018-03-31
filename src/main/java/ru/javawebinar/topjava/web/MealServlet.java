package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.Dates;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet{
    private static final Logger log = getLogger(UserServlet.class);



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        log.debug("redirect to meals");
        String redirectJsp = "/meals.jsp";
        String action = request.getParameter("action");
//        int id ;
        if(action != null){
            log.debug("action not null = " + action);
             int id = Integer.parseInt(request.getParameter("id"));

            switch (action){
                case "delete" : deleteMeal(id);
                                List<MealWithExceed> exceedMeal = MealsUtil.getFilteredWithExceeded(MealsUtil.concMeals,
                                        LocalTime.of(0, 1), LocalTime.of(23, 59), 2000);
                                request.setAttribute("list", exceedMeal);
                                redirectJsp = "/meals.jsp";
                                break;
                case "add" : redirectJsp = "/edit.jsp";break;
                case "edit": redirectJsp = "/edit.jsp";
                            Meal meal = getMeal(id);
                            request.setAttribute("meal",meal);
                            break;
            }
        }
        else {
            List<MealWithExceed> exceedMeal = MealsUtil.getFilteredWithExceeded(MealsUtil.concMeals,
                    LocalTime.of(0, 1), LocalTime.of(23, 59), 2000);
            log.debug(exceedMeal.toString());
            request.setAttribute("list", exceedMeal);
            redirectJsp = "/meals.jsp";
        }



        request.getRequestDispatcher(redirectJsp).forward(request, response);
//        response.sendRedirect("meals.jsp");
    }

    private void addMeal(Meal meal) {

        log.debug("in addMeal with meal " + meal);
        MealsUtil.addOrEditMeal(meal);
        log.debug("meal after edit got size " + MealsUtil.concMeals.size());

    }

    private Meal getMeal(int id) {
        log.debug("id = " + id);
        log.debug("in getMeal");
        return MealsUtil.concMeals.get(id);
    }

    private void deleteMeal(int id) {
        log.debug("id = " + id);
        log.debug("in deleteMeal");
        MealsUtil.concMeals.remove(id);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        log.debug("mealServlet doPost()");
        Meal meal = new Meal();
        meal.setDateTime(Dates.parseDateTimeLocal(req.getParameter("dateTime")));
        try{
            meal.setId(Integer.parseInt(req.getParameter("id")));
        }catch (Exception e){

        }

        meal.setCalories(Integer.parseInt(req.getParameter("calories")));
        meal.setDescription(req.getParameter("description"));
        log.debug("doPost() after geting meal " + meal);
        addMeal(meal);
//        resp.sendRedirect("meals.jsp");
        doGet(req,resp);
    }
}
