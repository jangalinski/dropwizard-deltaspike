package de.holisticon.dropwizard.deltaspike.example;

import org.apache.deltaspike.core.api.resourceloader.InjectableResource;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jangalinski on 10.01.15.
 */
public class MyServlet extends HttpServlet {

    @Inject
    private DummyResource.Bar bar;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print(bar.get());
    }
}
