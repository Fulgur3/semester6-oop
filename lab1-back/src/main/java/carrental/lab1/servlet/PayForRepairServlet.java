package carrental.lab1.servlet;

import carrental.lab1.util.ServletJsonMapper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import carrental.lab1.controller.RentController;

import java.math.BigDecimal;

@WebServlet(value = "/repair")
public class PayForRepairServlet extends HttpServlet {
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Request {
        public String hrnAmount;
        public int requestId;
    }

    private static class Response {
        public final boolean success;

        Response(boolean success) {
            this.success = success;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Request request = ServletJsonMapper.objectFromJsonRequest(req, Request.class);

        BigDecimal hrnAmount = new BigDecimal(request.hrnAmount);
        RentController.INSTANCE.payForRepair(request.requestId, hrnAmount);

        ServletJsonMapper.objectToJsonResponse(new Response(true), resp);
    }
}
