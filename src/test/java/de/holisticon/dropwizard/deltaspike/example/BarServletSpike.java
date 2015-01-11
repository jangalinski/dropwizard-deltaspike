package de.holisticon.dropwizard.deltaspike.example;

import org.apache.deltaspike.cdise.api.CdiContainer;
import org.apache.deltaspike.cdise.api.CdiContainerLoader;
import org.apache.deltaspike.cdise.servlet.CdiServletRequestListener;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.bridge.SLF4JBridgeHandler;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BarServletSpike extends HttpServlet {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    public static void main(String... args) throws Exception {
        CdiContainer cdiContainer = CdiContainerLoader.getCdiContainer();
        cdiContainer.boot();
        cdiContainer.getContextControl().startContexts();

        Server server = new Server(1234);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addEventListener(new CdiServletRequestListener());
        context.addServlet(BarServletSpike.class, "/*");

        server.start();
    }

    public static class Bar {
        public String hello() {
            return "bar";
        }
    }

    @Inject
    private Bar bar;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print(bar.hello());
    }
}
