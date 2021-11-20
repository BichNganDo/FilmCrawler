package servlets.client;

import common.Config;
import entity.film.Film;
import entity.film.FilterFilm;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.FilmModel;
import servlets.client.include.HeaderMenu;
import templater.PageGenerator;

public class Home extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_DOMAIN);

        //Trending
        FilterFilm filterFilmTrending = new FilterFilm();
        filterFilmTrending.setOffset(0);
        filterFilmTrending.setLimit(12);
        filterFilmTrending.setSearchStatus(1);
        filterFilmTrending.setSearchProperty(1);
        List<Film> listFilmByTrending = FilmModel.INSTANCE.getSliceFilm(filterFilmTrending);
        pageVariables.put("list_film_by_trend", listFilmByTrending);

        //Upcoming
        FilterFilm filterFilmUpcoming = new FilterFilm();
        filterFilmUpcoming.setOffset(0);
        filterFilmUpcoming.setLimit(12);
        filterFilmUpcoming.setSearchStatus(1);
        filterFilmUpcoming.setSearchProperty(2);
        List<Film> listFilmByUpcoming = FilmModel.INSTANCE.getSliceFilm(filterFilmUpcoming);
        pageVariables.put("list_film_by_upcoming", listFilmByUpcoming);

        //popular
        FilterFilm filterFilmpopular = new FilterFilm();
        filterFilmpopular.setOffset(0);
        filterFilmpopular.setLimit(12);
        filterFilmpopular.setSearchStatus(1);
        filterFilmpopular.setSearchProperty(4);
        List<Film> listFilmByPopular = FilmModel.INSTANCE.getSliceFilm(filterFilmpopular);
        pageVariables.put("list_film_by_popular", listFilmByPopular);

        //Header Menu
        pageVariables.put("header_menu", PageGenerator.instance().getPage("client/include/header_menu.html", HeaderMenu.INSTANCE.buildHeaderMenuData(request)));
        //Header - Footer
        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Config.STATIC_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));
        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", pageVariablesHeader));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/index.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
