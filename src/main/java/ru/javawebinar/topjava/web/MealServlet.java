package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController repository;
    private ConfigurableApplicationContext ctx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ctx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        repository = ctx.getBean(MealRestController.class);
    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("doGet()");
        int userId = AuthorizedUser.id();
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                repository.delete(userId,id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(userId,LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        repository.get(userId,getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                List<MealWithExceed> withFilter = MealsUtil.getWithExceeded(repository.getAll(userId),MealsUtil.DEFAULT_CALORIES_PER_DAY);

                if(isFilter(request,response)){
                    withFilter = getFinalList(withFilter,request,response);
                }

                request.setAttribute("meals", withFilter );
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        int userId = AuthorizedUser.id();
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String date = request.getParameter("startDate");
        String userSelected = request.getParameter("user-selected");
        if(id != null){
            log.debug("id != 0 ");
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    request.getParameter("userId").isEmpty() ? userId :
                            Integer.parseInt(request.getParameter("userId")),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.valueOf(request.getParameter("calories")));

            log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
            repository.save(userId,meal);
            response.sendRedirect("meals");
        }else if(date != null){
            log.debug("date != null - filtering");
            doGet(request, response);
        }else if(userSelected != null){
            log.debug("userSelected !=null : " + userSelected);
            int loggedUser = Integer.parseInt(userSelected);
            AuthorizedUser.setId(loggedUser);
            response.sendRedirect("meals");
        }

    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.valueOf(paramId);
    }
    private boolean isFilter(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        String date1 = request.getParameter("startDate");
        log.debug("isFilter()? " + (date1 == null ? "NO" : "YES"));
        if(date1 == null) return false;
        return true;
    }
    private List<MealWithExceed> getFinalList(List<MealWithExceed> noFilter,HttpServletRequest request,HttpServletResponse response){
        LocalDate startDate = null;
        LocalDate endDate = null;
        LocalTime startTime = null;
        LocalTime endTime = null;
        log.debug("noFilter after coming in method" + noFilter.toString());
        String date1 = request.getParameter("startDate");
        String date2 = request.getParameter("endDate");
        String time1 = request.getParameter("startTime");
        String time2 = request.getParameter("endTime");

        if(!date1.isEmpty() && !date2.isEmpty()){
            startDate = DateTimeUtil.parsingDate(date1);
            log.debug("startDate : " + startDate);
            endDate = DateTimeUtil.parsingDate(date2);
            log.debug("endDate : " + endDate);
        }


        if(!time1.isEmpty() && !time2.isEmpty()){
            startTime = DateTimeUtil.parsingTime(time1);
            log.debug("staRTtIME : " + startTime);
            endTime = DateTimeUtil.parsingTime(time2);
            log.debug("endTime : " + endTime);
        }
        List<MealWithExceed> filteredTime = null;
        List<MealWithExceed> filteredDate = null;
        int userId = AuthorizedUser.id();
        List<Meal> mealList = repository.getAll(userId);
        int calories = MealsUtil.DEFAULT_CALORIES_PER_DAY;


        if(startTime != null && endTime != null){
            filteredTime = MealsUtil.getFilteredWithExceeded(mealList,startTime,endTime,calories);
            log.debug("filteredTime : " + filteredTime);
        }
        if(endDate != null && startDate !=  null){
            filteredDate = MealsUtil.getFilteredWithExceeded(mealList,startDate,endDate,calories);
            log.debug("filteredDate : " + filteredDate);
        }
        log.debug("noFilter before retain" + noFilter.toString());

        if(filteredDate != null){
//           noFilter = retainList(noFilter,filteredDate);
            noFilter.retainAll(filteredDate);
        }
        if(filteredTime != null){
//            noFilter = retainList(noFilter,filteredTime);
            noFilter.retainAll(filteredTime);
        }
        return noFilter;
    }
    private List<MealWithExceed> retainList(List<MealWithExceed> list1,List<MealWithExceed> list2){
        List<MealWithExceed> returnedList = new ArrayList<>();
        list1.forEach(m -> {
            log.debug(" Contains element " + m  + "?");
            if(list2.contains(m)) {
                returnedList.add(m);
                log.debug("Yes it is for " + m);
            }
                });
        return returnedList;
    }
}
