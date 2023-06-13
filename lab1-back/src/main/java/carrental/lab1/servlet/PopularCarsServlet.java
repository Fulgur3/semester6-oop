package carrental.lab1.servlet;

import carrental.lab1.model.CarStatistic;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import carrental.lab1.controller.CarController;
import carrental.lab1.controller.UserController;
import carrental.lab1.util.ServletJsonMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@WebServlet(value = "/popular-cars")
public class PopularCarsServlet extends HttpServlet {
    private static class Request {
        public long token;
        public String since;
    }

    private static class Response {
        public final List<CarStatistic.RequestCount> carStatistics;

        Response(List<CarStatistic.RequestCount> carStatistics) {
            this.carStatistics = carStatistics;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Request request = ServletJsonMapper.objectFromJsonRequest(req, Request.class);

        if (!UserController.INSTANCE.isAdminToken(request.token)) {
            ServletJsonMapper.objectToJsonResponse(new Response(Collections.emptyList()), resp);
            return;
        }

        LocalDate since = null;
        if (request.since != null && request.since.length() > 0)
            since = LocalDate.parse(request.since);

        ServletJsonMapper.objectToJsonResponse(new Response(CarController.INSTANCE.getMostPopularCars(since)), resp);
    }
}
