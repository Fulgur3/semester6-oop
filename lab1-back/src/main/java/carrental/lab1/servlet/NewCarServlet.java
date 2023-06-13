package carrental.lab1.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import carrental.lab1.controller.CarController;
import carrental.lab1.controller.UserController;
import carrental.lab1.util.ServletJsonMapper;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(value = "/new-car")
public class NewCarServlet extends HttpServlet {
    private static class Request {
        public long token;
        public String model;
        public String manufacturer;
        public String hrnPerDay;
        public String thumbnailUrl;
        public String description;
        public String hrnPurchase;
    }

    private static class Response {
        public final boolean success;

        Response(boolean success) {
            this.success = success;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Request request = ServletJsonMapper.objectFromJsonRequest(req, Request.class);

        if (!UserController.INSTANCE.isAdminToken(request.token)) {
            ServletJsonMapper.objectToJsonResponse(new Response(false), resp);
            return;
        }

        BigDecimal hrnPerDay = new BigDecimal(request.hrnPerDay);
        BigDecimal hrnPurchase = new BigDecimal(request.hrnPurchase);
        CarController.INSTANCE.addCar(
                request.model,
                request.manufacturer,
                hrnPerDay,
                null,
                request.thumbnailUrl,
                request.description,
                hrnPurchase
        );

        ServletJsonMapper.objectToJsonResponse(new Response(true), resp);
    }
}
