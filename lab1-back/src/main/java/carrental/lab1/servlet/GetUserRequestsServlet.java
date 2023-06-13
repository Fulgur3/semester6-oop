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

@WebServlet(value = "/my-requests")
public class GetUserRequestsServlet extends HttpServlet {
    private static class Request {
        public long token;
        public int status;
        public boolean getOutdatedActive;
    }

    private static class Response {
        public final RequestInfo[] requests;

        Response(RequestInfo[] requestInfos) {
            this.requests = requestInfos;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Request request = ServletJsonMapper.objectFromJsonRequest(req, Request.class);

        long userId = UserController.INSTANCE.getUserIdFromToken(request.token);

        List<RequestInfo> requestInfoList = request.getOutdatedActive ?
                RentController.INSTANCE.getActiveOutdatedRequests(userId)
                : RentController.INSTANCE.getRequestsWithStatusForUser(request.status, userId);
        RequestInfo[] requestInfos = new RequestInfo[requestInfoList.size()];
        requestInfoList.toArray(requestInfos);

        ServletJsonMapper.objectToJsonResponse(new Response(requestInfos), resp);
    }
}

