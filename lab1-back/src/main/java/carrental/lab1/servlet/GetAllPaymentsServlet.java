package carrental.lab1.servlet;

import carrental.lab1.model.PaymentInfo;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import carrental.lab1.controller.PaymentController;
import carrental.lab1.controller.UserController;
import carrental.lab1.util.ServletJsonMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@WebServlet(value = "/all-payments")
public class GetAllPaymentsServlet extends HttpServlet {
    private static class Request {
        public long token;
        public String since;
        public String carId;
    }

    private static class Response {
        public final List<PaymentInfo> payments;

        Response(List<PaymentInfo> payments) {
            this.payments = payments;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Request request = ServletJsonMapper.objectFromJsonRequest(req, Request.class);

        if (!UserController.INSTANCE.isAdminToken(request.token)) {
            ServletJsonMapper.objectToJsonResponse(new Response(Collections.emptyList()), resp);
            return;
        }

        if (request.carId != null && request.carId.length() > 0) {
            int carId = Integer.parseInt(request.carId);
            ServletJsonMapper.objectToJsonResponse(new Response(PaymentController.INSTANCE.getAllPaymentInfo(carId)), resp);
        } else {
            LocalDate since = null;
            if (request.since != null && request.since.length() > 0)
                since = LocalDate.parse(request.since);

            ServletJsonMapper.objectToJsonResponse(new Response(PaymentController.INSTANCE.getAllPaymentInfo(since)), resp);
        }
    }
}
