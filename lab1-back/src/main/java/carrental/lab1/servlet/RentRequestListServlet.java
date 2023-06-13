package carrental.lab1.servlet;

import carrental.lab1.model.RequestInfo;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import carrental.lab1.controller.RentController;
import carrental.lab1.controller.UserController;
import carrental.lab1.util.ServletJsonMapper;

import java.util.List;

@WebServlet(value = "/list-requests", name = "listRequests")
public class RentRequestListServlet extends HttpServlet {
    private static class Request {
        public long token;
        public int status;
        public boolean getOutdatedActive;
    }

    private static class Response {
        public final RequestInfo[] requests;

        Response(RequestInfo[] requests) {
            this.requests = requests;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Request request = ServletJsonMapper.objectFromJsonRequest(req, Request.class);

        if (!UserController.INSTANCE.isAdminToken(request.token)) {
            ServletJsonMapper.objectToJsonResponse(new Response(new RequestInfo[0]), resp);
            return;
        }

        List<RequestInfo> rentRequestList = request.getOutdatedActive ?
                RentController.INSTANCE.getActiveOutdatedRequests()
                : RentController.INSTANCE.getRequestsWithStatus(request.status);
        RequestInfo[] rentRequests = new RequestInfo[rentRequestList.size()];
        rentRequestList.toArray(rentRequests);

        ServletJsonMapper.objectToJsonResponse(new Response(rentRequests), resp);
    }
}
