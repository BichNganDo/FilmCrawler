package servlets.client.include;

import common.Config;
import entity.cate_film.CategoryFilm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.CategoryFilmModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class HeaderMenu {

    public static HeaderMenu INSTANCE = new HeaderMenu();

    public Map<String, Object> buildHeaderMenuData(HttpServletRequest request) {
        Map<String, Object> pageVariablesHeaderMenu = new HashMap<>();
        pageVariablesHeaderMenu.put("app_domain", Config.APP_DOMAIN);
        pageVariablesHeaderMenu.put("static_domain", Config.STATIC_DOMAIN);

        String url = request.getRequestURL().toString();
        String[] urlSplit = url.split("/");
        String nameSlug = urlSplit[urlSplit.length - 1];

        int idCate = CategoryFilmModel.INSTANCE.getIdCateByNameSlug(nameSlug);
        pageVariablesHeaderMenu.put("id_cate", idCate);

        List<CategoryFilm> listCateFilm = CategoryFilmModel.INSTANCE.getSliceCateFilm(0, 20, "", 1);
        pageVariablesHeaderMenu.put("list_cate_film", listCateFilm);

        String query = request.getParameter("query");
        if (StringUtils.isEmpty(query)) {
            query = "";
        }

        pageVariablesHeaderMenu.put("query", query.toLowerCase());

        return pageVariablesHeaderMenu;
    }
}
