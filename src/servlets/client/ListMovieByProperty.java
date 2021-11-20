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
import org.apache.commons.lang3.math.NumberUtils;
import servlets.client.include.HeaderMenu;
import templater.PageGenerator;

public class ListMovieByProperty extends HttpServlet {

    private int DEFAULT_ITEM_PER_PAGE = 18;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_DOMAIN);

        int itemPerPage = NumberUtils.toInt(request.getParameter("limit"), DEFAULT_ITEM_PER_PAGE);
        if (itemPerPage < 0 && itemPerPage > 50) {
            itemPerPage = DEFAULT_ITEM_PER_PAGE;
        }
        pageVariables.put("item_per_page", itemPerPage);

        String type = request.getParameter("type");
        pageVariables.put("type", type);

        FilterFilm filterFilmByProperty = new FilterFilm();
        if ("thinh-hanh".equalsIgnoreCase(type)) {
            filterFilmByProperty.setSearchProperty(1);
        } else if ("sap-chieu".equalsIgnoreCase(type)) {
            filterFilmByProperty.setSearchProperty(2);
        } else {
            filterFilmByProperty.setSearchProperty(4);
        }

        int totalFilmByProperty = FilmModel.INSTANCE.getTotalProduct(filterFilmByProperty);
        pageVariables.put("total_film_by_property", totalFilmByProperty);

        int totalPage = (int) Math.ceil((double) totalFilmByProperty / itemPerPage);
        pageVariables.put("total_page", totalPage);

        int page = NumberUtils.toInt(request.getParameter("page"), 1);
        if (page > totalPage) {
            page = totalPage;
        }
        if (page <= 0) {
            page = 1;
        }
        pageVariables.put("current_page", page);

        int offset = (page - 1) * itemPerPage;

        FilterFilm filterFilm = new FilterFilm();
        filterFilm.setOffset(offset);
        filterFilm.setLimit(itemPerPage);
        filterFilm.setSearchStatus(1);
        if ("thinh-hanh".equalsIgnoreCase(type)) {
            filterFilm.setSearchProperty(1);
        } else if ("sap-chieu".equalsIgnoreCase(type)) {
            filterFilm.setSearchProperty(2);
        } else {
            filterFilm.setSearchProperty(4);
        }

        List<Film> listFilmByProperty = FilmModel.INSTANCE.getSliceFilm(filterFilm);
        pageVariables.put("list_film_by_property", listFilmByProperty);

        //Header Menu
        pageVariables.put("header_menu", PageGenerator.instance().getPage("client/include/header_menu.html", HeaderMenu.INSTANCE.buildHeaderMenuData(request)));
        //Header - Footer
        Map<String, Object> pageVariablesHeader = new HashMap<>();
        pageVariablesHeader.put("static_domain", Config.STATIC_DOMAIN);
        pageVariables.put("header_include", PageGenerator.instance().getPage("client/include/header.html", pageVariablesHeader));
        pageVariables.put("footer_include", PageGenerator.instance().getPage("client/include/footer.html", pageVariablesHeader));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("client/list_movie_by_property.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
