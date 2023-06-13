package carrental.lab1.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import carrental.lab1.controller.CarController;
import carrental.lab1.util.ServletJsonMapper;

import java.util.List;

@WebServlet(value = "/car-models")
public class ListCarManufacturersServlet extends HttpServlet {
    private static class Response {
        public final List<String> manufacturers;

        Response(List<String> manufacturers) {
            this.manufacturers = manufacturers;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        ServletJsonMapper.objectToJsonResponse(new Response(CarController.INSTANCE.getAllCarManufacturers()), resp);
    }
}
