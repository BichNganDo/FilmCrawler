package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.cate_film.CategoryFilm;
import entity.cate_film.ListCateFilm;
import helper.ServletUtil;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.CategoryFilmModel;
import org.apache.commons.lang3.math.NumberUtils;

public class ApiCateFilmServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getcatefilm": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchStatus = NumberUtils.toInt(request.getParameter("search_status"));

                int offset = (pageIndex - 1) * limit;
                List<CategoryFilm> listCateFilm = CategoryFilmModel.INSTANCE.getSliceCateFilm(offset, limit, searchQuery, searchStatus);
                int totalCateFilm = CategoryFilmModel.INSTANCE.getTotalCateFilm(searchQuery, searchStatus);

                ListCateFilm listCategoryFilm = new ListCateFilm();
                listCategoryFilm.setTotal(totalCateFilm);
                listCategoryFilm.setListCateFilm(listCateFilm);
                listCategoryFilm.setItemPerPage(10);

                if (listCateFilm.size() >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listCategoryFilm);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getCateFilmById": {
                int idCateFilm = NumberUtils.toInt(request.getParameter("id_cate_film"));

                CategoryFilm cateFilmByID = CategoryFilmModel.INSTANCE.getCategoryFilmByID(idCateFilm);

                if (cateFilmByID.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(cateFilmByID);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }

            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "add": {
                String name = request.getParameter("name");
                int status = NumberUtils.toInt(request.getParameter("status"));

                int addCateFilm = CategoryFilmModel.INSTANCE.addCategoryFlm(name, status);

                if (addCateFilm >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Thêm Cate Film thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Thêm Cate Film thất bại!");
                }
                break;
            }
            case "edit": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                String name = request.getParameter("name");
                int status = NumberUtils.toInt(request.getParameter("status"));

                CategoryFilm cateFilmById = CategoryFilmModel.INSTANCE.getCategoryFilmByID(id);
                if (cateFilmById.getId() == 0) {
                    result.setErrorCode(-1);
                    result.setMessage("Thất bại!");
                    return;
                }

                int editCateFilm = CategoryFilmModel.INSTANCE.editCategoryFilm(id, name, status);

                if (editCateFilm >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Sửa category film thành công!");
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Sửa category film thất bại!");
                }
                break;
            }

            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteCateFilm = CategoryFilmModel.INSTANCE.deleteCategoryFilm(id);
                if (deleteCateFilm >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa category film thành công!");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa category film thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));

    }
}
