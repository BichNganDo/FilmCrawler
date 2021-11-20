package servlets.client;

import common.Config;
import entity.film.Film;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.FilmModel;
import org.apache.commons.lang3.math.NumberUtils;
import servlets.client.include.HeaderMenu;
import templater.PageGenerator;

public class MovieSingle extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_DOMAIN);

        int id = NumberUtils.toInt(request.getParameter("id"));
        pageVariables.put("id_film", id);
        Film filmById = FilmModel.INSTANCE.getFilmByID(id);
        pageVariables.put("film_by_id", filmById);

        //Header Menu
        pageVariables.put("header_menu", PageGenerator.instance().getPage("client/include/header_menu.html", HeaderMenu.INSTANCE.buildHeaderMenuData(request)));
        //Header - Footer
        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Config.STATIC_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));
        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", pageVariablesHeader));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/moviesingle.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
