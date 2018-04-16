package com.company.revolut;

import com.company.revolut.builder.ResponseBuilder;
import com.company.revolut.configuration.EntityManagerConfiguration;
import com.company.revolut.controller.AccountController;
import com.company.revolut.controller.TransactionController;
import com.company.revolut.service.dao.AccountDaoAdapter;
import com.company.revolut.service.dao.TransactionDaoAdapter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.persistence.EntityManager;

public class Application {

    public static void main(String[] args) {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(8080);
        jettyServer.setHandler(context);

        ServletContainer servletContainer = new ServletContainer(createResourceConfig());
        ServletHolder jerseyServlet = new ServletHolder(servletContainer);
        context.addServlet(jerseyServlet, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter("jersey.config.server.provider.packages", "com.company.revolut.controller");

        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jettyServer.destroy();
        }
    }

    public static ResourceConfig createResourceConfig() {
        return new ResourceConfig()
                .packages("com.company.revolut")
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bindFactory(new EntityManagerConfiguration()).to(EntityManager.class).in(RequestScoped.class);
                    }
                })
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(new ResponseBuilder()).to(ResponseBuilder.class);
                    }
                })
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(new AccountDaoAdapter(new EntityManagerConfiguration().provide())).to(AccountDaoAdapter.class);
                    }
                })
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(new TransactionDaoAdapter(new EntityManagerConfiguration().provide())).to(TransactionDaoAdapter.class);
                    }
                })
                .register(AccountController.class)
                .register(TransactionController.class);
    }

}
