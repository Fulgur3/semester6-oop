package carrental.lab1.servlet;

import carrental.lab1.controller.UserController;
import carrental.lab1.util.ServletJsonMapper;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(value = "/register", name = "registerServlet")
public class RegisterServlet extends HttpServlet {
    private static class Request {
        public long passportId;
        public String username;
        public String password;
    }

    private static class Response {
        public final String token;
        public final boolean isAdmin;

        Response(String token, boolean isAdmin) {
            this.token = token;
            this.isAdmin = isAdmin;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        Request request = ServletJsonMapper.objectFromJsonRequest(req, Request.class);

        Long token = UserController.INSTANCE.registerAndLogIn(request.passportId, request.username, request.password);

        if (token != null) {
            boolean isAdmin = UserController.INSTANCE.isAdminToken(token);
            ServletJsonMapper.objectToJsonResponse(new Response(token.toString(), isAdmin), resp);
        } else {
            ServletJsonMapper.objectToJsonResponse(new Response("", false), resp);
        }
    }
}
