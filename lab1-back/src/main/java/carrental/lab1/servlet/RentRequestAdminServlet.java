package carrental.lab1.servlet;

import carrental.lab1.util.ServletJsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import carrental.lab1.controller.RentController;
import carrental.lab1.controller.UserController;

import java.math.BigDecimal;

@WebServlet(value = "/admin-request", name = "adminRequest")
public class RentRequestAdminServlet extends HttpServlet {
    private static final String ACTION_APPROVE = "approve";
    private static final String ACTION_DENY = "deny";
    private static final String ACTION_SET_NEEDS_REPAIR = "needs-repair";
    private static final String ACTION_END_SUCCESSFULLY = "end";

    private static class Request {
        public long token;
        public int requestId;
        public String action;
        public String actionMessage;
        public String repairCostHrn;
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

        if (!UserController.INSTANCE.isAdminToken(request.token)) {
            ServletJsonMapper.objectToJsonResponse(new Response(false), resp);
            return;
        }

        RentController rentController = RentController.INSTANCE;

        switch (request.action) {
            case ACTION_APPROVE -> rentController.approve(request.requestId);
            case ACTION_DENY -> rentController.deny(request.requestId, request.actionMessage);
            case ACTION_END_SUCCESSFULLY -> {
                BigDecimal repairCostHrn = new BigDecimal(request.repairCostHrn);
                rentController.endSuccessfully(request.requestId, repairCostHrn);
            }
            case ACTION_SET_NEEDS_REPAIR -> {
                BigDecimal repairCostHrn = new BigDecimal(request.repairCostHrn);
                rentController.setNeedsRepair(request.requestId, request.actionMessage, repairCostHrn);
            }
        }
        ServletJsonMapper.objectToJsonResponse(new Response(true), resp);
    }
}
