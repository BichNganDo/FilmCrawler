package servlets.client.include;

import common.Config;
import entity.cate_film.CategoryFilm;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.CategoryFilmModel;

public class HeaderMenu {

    public static HeaderMenu INSTANCE = new HeaderMenu();

    public Map<String, Object> buildHeaderMenuData(HttpServletRequest request) {
        Map<String, Object> pageVariablesHeaderMenu = new HashMap<>();
        pageVariablesHeaderMenu.put("app_domain", Config.APP_DOMAIN);
        pageVariablesHeaderMenu.put("static_domain", Config.STATIC_DOMAIN);

        List<CategoryFilm> listCateFilm = CategoryFilmModel.INSTANCE.getSliceCateFilm(0, 20, "", 1);
        pageVariablesHeaderMenu.put("list_cate_film", listCateFilm);

        return pageVariablesHeaderMenu;
    }
}
