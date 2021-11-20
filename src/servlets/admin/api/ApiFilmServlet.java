package servlets.admin.api;

import com.google.gson.Gson;
import common.APIResult;
import entity.film.Film;
import entity.film.FilterFilm;
import entity.film.ListFilm;
import helper.ServletUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.FilmModel;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

public class ApiFilmServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Gson gson = new Gson();
        APIResult result = new APIResult(0, "Success");

        String action = request.getParameter("action");
        switch (action) {
            case "getfilm": {
                int pageIndex = NumberUtils.toInt(request.getParameter("page_index"));
                int limit = NumberUtils.toInt(request.getParameter("limit"), 10);
                String searchQuery = request.getParameter("search_query");
                int searchStatus = NumberUtils.toInt(request.getParameter("search_status"));
                int searchCate = NumberUtils.toInt(request.getParameter("search_cate"));
                int searchProperty = NumberUtils.toInt(request.getParameter("search_property"));
                int offset = (pageIndex - 1) * limit;

                FilterFilm filterFilm = new FilterFilm();
                filterFilm.setLimit(limit);
                filterFilm.setOffset(offset);
                filterFilm.setSearchQuery(searchQuery);
                filterFilm.setSearchCate(searchCate);
                filterFilm.setSearchProperty(searchProperty);
                filterFilm.setSearchStatus(searchStatus);

                List<Film> listFilm = FilmModel.INSTANCE.getSliceFilm(filterFilm);
                int totalFilm = FilmModel.INSTANCE.getTotalProduct(filterFilm);

                ListFilm listFilms = new ListFilm();
                listFilms.setTotal(totalFilm);
                listFilms.setListFilm(listFilm);
                listFilms.setItemPerPage(10);

                if (listFilm.size() >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(listFilms);
                } else {
                    result.setErrorCode(-1);
                    result.setMessage("Fail");
                }
                break;
            }
            case "getFilmById": {
                int idFilm = NumberUtils.toInt(request.getParameter("id_film"));

                Film filmById = FilmModel.INSTANCE.getFilmByID(idFilm);

                if (filmById.getId() > 0) {
                    result.setErrorCode(0);
                    result.setMessage("Success");
                    result.setData(filmById);
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
                try {
                    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                    if (isMultipart) {
                        Film film = new Film();
                        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                        upload.setHeaderEncoding("UTF-8");

                        List<FileItem> items = upload.parseRequest(request);
                        for (FileItem item : items) {
                            if (item.isFormField()) {
                                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                                String fieldname = item.getFieldName();
                                String fieldvalue = item.getString("UTF-8");

                                switch (fieldname) {
                                    case "category": {
                                        film.setIdCate(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "title": {
                                        film.setTitle(fieldvalue);
                                        break;
                                    }
                                    case "content": {
                                        film.setContent(fieldvalue);
                                        break;
                                    }
                                    case "duration": {
                                        film.setDuration(fieldvalue);
                                        break;
                                    }
                                    case "openingDay": {
                                        film.setOpenDay(fieldvalue);
                                        break;
                                    }
                                    case "trailer": {
                                        film.setTrailer(fieldvalue);
                                        break;
                                    }
                                    case "status": {
                                        film.setStatus(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "property": {
                                        film.setProperty(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }

                                }

                            } else {
                                // Process form file field (input type="file").
                                String filename = FilenameUtils.getName(item.getName());
                                InputStream a = item.getInputStream();
                                Path uploadDir = Paths.get("upload/poster_film/" + filename);
                                Files.copy(a, uploadDir, StandardCopyOption.REPLACE_EXISTING);
                                film.setPoster("upload/poster_film/" + filename);
                            }
                        }

                        int addFilm = FilmModel.INSTANCE.addFilm(film);

                        if (addFilm >= 0) {
                            result.setErrorCode(0);
                            result.setMessage("Them film thanh cong!");
                        } else {
                            result.setErrorCode(-1);
                            result.setMessage("Them film that bai!");
                        }
                    } else {
                        result.setErrorCode(-4);
                        result.setMessage("Có lỗi");
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            }

            case "edit": {
                try {
                    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                    if (isMultipart) {
                        Film film = new Film();
                        String oldImagePoster = "";
                        String newImagePoster = "";
                        ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
                        upload.setHeaderEncoding("UTF-8");

                        List<FileItem> items = upload.parseRequest(request);
                        for (FileItem item : items) {
                            if (item.isFormField()) {
                                // Process regular form field (input type="text|radio|checkbox|etc", select, etc).
                                String fieldname = item.getFieldName();
                                String fieldvalue = item.getString("UTF-8");

                                switch (fieldname) {
                                    case "id": {
                                        film.setId(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "category": {
                                        film.setIdCate(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "title": {
                                        film.setTitle(fieldvalue);
                                        break;
                                    }
                                    case "content": {
                                        film.setContent(fieldvalue);
                                        break;
                                    }
                                    case "duration": {
                                        film.setDuration(fieldvalue);
                                        break;
                                    }
                                    case "opening_day": {
                                        film.setOpenDay(fieldvalue);
                                        break;
                                    }
                                    case "trailer": {
                                        film.setTrailer(fieldvalue);
                                        break;
                                    }
                                    case "status": {
                                        film.setStatus(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "property": {
                                        film.setProperty(NumberUtils.toInt(fieldvalue));
                                        break;
                                    }
                                    case "poster": {
                                        oldImagePoster = fieldvalue;
                                        break;
                                    }

                                }

                            } else {
                                // Process form file field (input type="file").
                                String filename = FilenameUtils.getName(item.getName());
                                InputStream a = item.getInputStream();
                                Path uploadDir = Paths.get("C:\\Users\\Ngan Do\\Documents\\NetBeansProjects\\FilmCrawler\\upload\\poster_film\\" + filename);
                                Files.copy(a, uploadDir, StandardCopyOption.REPLACE_EXISTING);
                                newImagePoster = "upload\\poster_film\\" + filename;
                            }
                        }

                        if (StringUtils.isNotEmpty(newImagePoster)) {
                            film.setPoster(newImagePoster);
                        } else {
                            film.setPoster(oldImagePoster);
                        }

                        Film filmById = FilmModel.INSTANCE.getFilmByID(film.getId());
                        if (filmById.getId() == 0) {
                            result.setErrorCode(-1);
                            result.setMessage("Thất bại");
                            return;
                        }

                        int editFilm = FilmModel.INSTANCE.editFilm(film);

                        if (editFilm >= 0) {
                            result.setErrorCode(0);
                            result.setMessage("Sua film thanh cong!");
                        } else {
                            result.setErrorCode(-1);
                            result.setMessage("Sua film that bai");
                        }
                    } else {
                        result.setErrorCode(-4);
                        result.setMessage("Có lỗi");
                    }

//                    
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                break;
            }
            case "delete": {
                int id = NumberUtils.toInt(request.getParameter("id"));
                int deleteFilm = FilmModel.INSTANCE.deleteFilm(id);
                if (deleteFilm >= 0) {
                    result.setErrorCode(0);
                    result.setMessage("Xóa Film thành công1");
                } else {
                    result.setErrorCode(-2);
                    result.setMessage("Xóa Film thất bại!");
                }
                break;
            }
            default:
                throw new AssertionError();
        }

        ServletUtil.printJson(request, response, gson.toJson(result));
    }

}
