package carrental.lab1.servlet;

import carrental.lab1.util.ServletJsonMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import carrental.lab1.controller.RentController;
import carrental.lab1.controller.UserController;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

@WebServlet(value = "/new-request", name = "newRequest")
public class RentRequestNewServlet extends HttpServlet {
    private static class Request {
        public long token;
        public int carId;
        public int days;
        public String startDate;
        public String hrnAmount;
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

        LocalDate startDate = LocalDate.parse(request.startDate);
        long userId = UserController.INSTANCE.getUserIdFromToken(request.token);

        BigDecimal hrnAmount = new BigDecimal(request.hrnAmount);
        RentController.INSTANCE.addNewPending(userId, request.carId, request.days, startDate, hrnAmount);

        ServletJsonMapper.objectToJsonResponse(new Response(true), resp);
    }
}
