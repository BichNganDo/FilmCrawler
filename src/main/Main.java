package main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import servlets.admin.PartialServlet;
import servlets.admin.UploadFileServlet;
import servlets.admin.api.ApiCateFilmServlet;
import servlets.admin.api.ApiFilmServlet;
import servlets.admin.cate_film.AddCategoryFilm;
import servlets.admin.cate_film.EditCategoryFilm;
import servlets.admin.cate_film.ManageCategoryFilm;
import servlets.admin.film.AddFilm;
import servlets.admin.film.EditFilm;
import servlets.admin.film.ManageFilm;
import servlets.client.ListMovieByProperty;
import servlets.client.MovieGrid;
import servlets.client.MovieSingle;

public class Main {

    public static void main(String[] args) throws Exception {

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        //<editor-fold defaultstate="collapsed" desc="admin">
        context.addServlet(new ServletHolder(new PartialServlet()), "/admin/partital/*");

        context.addServlet(new ServletHolder(new ManageCategoryFilm()), "/admin/cate_film");
        context.addServlet(new ServletHolder(new ManageFilm()), "/admin/film");

        context.addServlet(new ServletHolder(new AddCategoryFilm()), "/admin/cate_film/add");
        context.addServlet(new ServletHolder(new AddFilm()), "/admin/film/add");

        context.addServlet(new ServletHolder(new EditCategoryFilm()), "/admin/cate_film/edit");
        context.addServlet(new ServletHolder(new EditFilm()), "/admin/film/edit");

        context.addServlet(new ServletHolder(new ApiCateFilmServlet()), "/admin/api/cate_film");
        context.addServlet(new ServletHolder(new ApiFilmServlet()), "/admin/api/film");

        context.addServlet(new ServletHolder(new UploadFileServlet()), "/admin/upload-file");
//</editor-fold>

        context.addServlet(new ServletHolder(new MovieGrid()), "/the-loai");
        context.addServlet(new ServletHolder(new ListMovieByProperty()), "/");
        context.addServlet(new ServletHolder(new MovieSingle()), "/chi-tiet-phim");

        ContextHandler resourceHandler = new ContextHandler("/static");
        String resource = "./public";
        if (!resource.isEmpty()) {
            resourceHandler.setResourceBase(resource);
            resourceHandler.setHandler(new ResourceHandler());
        }

        ContextHandler uploadHandler = new ContextHandler("/upload");
        String resourceUpload = "./upload";
        uploadHandler.setResourceBase(resourceUpload);
        uploadHandler.setHandler(new ResourceHandler());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, uploadHandler, context});

        Server server = new Server(8080);

        server.setHandler(handlers);

        server.start();

        System.out.println("Server started");

        server.join();
    }
}
