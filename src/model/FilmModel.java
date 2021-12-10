package model;

import client.MysqlClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import common.ErrorCode;
import entity.film.Film;
import entity.film.FilterFilm;
import helper.ServletUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;

public class FilmModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "film";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    LoadingCache<Integer, Film> filmCache = null;
    private Map<Integer, Film> CACHE = new HashMap<>();
    public static FilmModel INSTANCE = new FilmModel();

    private FilmModel() {
        filmCache = CacheBuilder.newBuilder()
                .maximumSize(100) // maximum 100 records can be cached
                .expireAfterAccess(30, TimeUnit.MINUTES) // cache will expire after 30 minutes of access
                .build(new CacheLoader<Integer, Film>() {  // build the cacheloader
                    @Override
                    public Film load(Integer id) throws Exception {
                        //make the expensive call
                        return INSTANCE.getFilmByID(id);
                    }
                });
    }

    public List<Film> getSliceFilm(FilterFilm filterFilm) {
        List<Film> resultListFilm = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                System.out.println("Loi ket noi MySql Loi ket noi MySql Loi ket noi MySql");
                return resultListFilm;

            }
            String sql = "SELECT film.*, cate_film.name AS `category` "
                    + "FROM film "
                    + "INNER JOIN cate_film ON film.id_cate= cate_film.id "
                    + "WHERE 1=1";

            if (StringUtils.isNotEmpty(filterFilm.getSearchQuery())) {
                sql = sql + " AND film.title LIKE ? ";
            }

            if (filterFilm.getSearchCate() > 0) {
                sql = sql + " AND film.id_cate = ? ";
            }
            if (filterFilm.getSearchStatus() > 0) {
                sql = sql + " AND film.status = ? ";
            }
            if (filterFilm.getSearchProperty() > 0) {
                sql = sql + " AND film.property = ? ";
            }

            sql = sql + " ORDER BY created_date DESC LIMIT ? OFFSET ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(filterFilm.getSearchQuery())) {
                ps.setString(param++, "%" + filterFilm.getSearchQuery() + "%");
            }

            if (filterFilm.getSearchCate() > 0) {
                ps.setInt(param++, filterFilm.getSearchCate());
            }
            if (filterFilm.getSearchStatus() > 0) {
                ps.setInt(param++, filterFilm.getSearchStatus());
            }

            if (filterFilm.getSearchProperty() > 0) {
                ps.setInt(param++, filterFilm.getSearchProperty());
            }

            ps.setInt(param++, filterFilm.getLimit());
            ps.setInt(param++, filterFilm.getOffset());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setIdCate(rs.getInt("id_cate"));
                film.setCategory(rs.getString("category"));
                //category Slug
                String categorySlug = ServletUtil.toSlug(film.getCategory());
                film.setCategorySlug(categorySlug);
                film.setTitle(rs.getString("title"));
                //title Slug
                String titleSlug = ServletUtil.toSlug(film.getTitle());
                film.setTitleSlug(titleSlug + "-" + film.getId() + ".html");

                film.setContent(rs.getString("content"));
                film.setPosterUrlWithBaseDomain(rs.getString("poster"));
                film.setDuration(rs.getString("duration"));
                film.setOpenDay(rs.getString("opening_day"));
                film.setTrailer(rs.getString("trailer"));
                film.setStatus(rs.getInt("status"));
                film.setProperty(rs.getInt("property"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                film.setCreatedDate(dateString);

                film.setDirector(rs.getString("director"));
                film.setCountry(rs.getString("country"));
                film.setScore(rs.getString("score"));
                film.setLinkWatch(rs.getString("link_watch"));

                resultListFilm.add(film);
            }

            return resultListFilm;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListFilm;
    }

    public int getTotalProduct(FilterFilm filterFilm) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }

            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1 = 1";
            if (StringUtils.isNotEmpty(filterFilm.getSearchQuery())) {
                sql = sql + " AND film.title LIKE ? ";
            }

            if (filterFilm.getSearchCate() > 0) {
                sql = sql + " AND film.id_cate = ? ";
            }

            if (filterFilm.getSearchProperty() > 0) {
                sql = sql + " AND film.property = ? ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(filterFilm.getSearchQuery())) {
                ps.setString(param++, "%" + filterFilm.getSearchQuery() + "%");
            }

            if (filterFilm.getSearchCate() > 0) {
                ps.setInt(param++, filterFilm.getSearchCate());
            }

            if (filterFilm.getSearchProperty() > 0) {
                ps.setInt(param++, filterFilm.getSearchProperty());
            }

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return total;
    }

    public Film getFilmByID(int id) {
        Film result = new Film();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getProductByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` "
                    + " INNER JOIN cate_film ON film.id_cate = cate_film.id"
                    + " WHERE film.id = ? ");
            getProductByIdStmt.setInt(1, id);

            ResultSet rs = getProductByIdStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setIdCate(rs.getInt("id_cate"));
                result.setCategory(rs.getString("cate_film.name"));
                result.setTitle(rs.getString("title"));
                result.setPosterUrlWithBaseDomain(rs.getString("poster"));
                result.setContent(rs.getString("content"));
                result.setDuration(rs.getString("duration"));
                result.setOpenDay(rs.getString("opening_day"));
                result.setTrailer(rs.getString("trailer"));
                result.setProperty(rs.getInt("property"));
                result.setStatus(rs.getInt("status"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                result.setCreatedDate(dateString);
                result.setDirector(rs.getString("director"));
                result.setCountry(rs.getString("country"));
                result.setScore(rs.getString("score"));
                result.setLinkWatch(rs.getString("link_watch"));
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public Film getFilmByIDWithGuava(int id) {
        try {
            return filmCache.get(id);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public Film getFilmByIdWithCache(int id) {
        Film result = new Film();
        if (CACHE.containsKey(id)) {
            return CACHE.get(id);
        } else {
            Connection conn = null;
            try {
                conn = dbClient.getDbConnection();
                if (null == conn) {
                    return result;
                }
                PreparedStatement getProductByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` "
                        + " INNER JOIN cate_film ON film.id_cate = cate_film.id"
                        + " WHERE film.id = ? ");
                getProductByIdStmt.setInt(1, id);

                ResultSet rs = getProductByIdStmt.executeQuery();

                if (rs.next()) {
                    result.setId(rs.getInt("id"));
                    result.setIdCate(rs.getInt("id_cate"));
                    result.setCategory(rs.getString("cate_film.name"));
                    result.setTitle(rs.getString("title"));
                    result.setPosterUrlWithBaseDomain(rs.getString("poster"));
                    result.setContent(rs.getString("content"));
                    result.setDuration(rs.getString("duration"));
                    result.setOpenDay(rs.getString("opening_day"));
                    result.setTrailer(rs.getString("trailer"));
                    result.setProperty(rs.getInt("property"));
                    result.setStatus(rs.getInt("status"));

                    long currentTimeMillis = rs.getLong("created_date");
                    Date date = new Date(currentTimeMillis);
                    String dateString = sdf.format(date);
                    result.setCreatedDate(dateString);
                    result.setDirector(rs.getString("director"));
                    result.setCountry(rs.getString("country"));
                    result.setScore(rs.getString("score"));
                    result.setLinkWatch(rs.getString("link_watch"));
                }
                CACHE.put(id, result);
                return result;

            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                dbClient.releaseDbConnection(conn);
            }
            return result;
        }

    }

    public boolean isExistFilm(String poster) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return false;
            }

            PreparedStatement isExistFilmStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE poster = ?");
            isExistFilmStmt.setString(1, poster);

            ResultSet rs = isExistFilmStmt.executeQuery();
            if (rs.next()) {
                return true;
            }

            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return false;
    }

    public int addFilm(Film film) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (id_cate, title, poster, score, "
                    + "content, duration, opening_day, trailer, director, country, status, property, created_date, link_watch) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            addStmt.setInt(1, film.getIdCate());
            addStmt.setString(2, film.getTitle());
            addStmt.setString(3, film.getPoster());
            addStmt.setString(4, film.getScore());
            addStmt.setString(5, film.getContent());
            addStmt.setString(6, film.getDuration());
            addStmt.setString(7, film.getOpenDay());
            addStmt.setString(8, film.getTrailer());
            addStmt.setString(9, film.getDirector());
            addStmt.setString(10, film.getCountry());
            addStmt.setInt(11, film.getStatus());
            addStmt.setInt(12, film.getProperty().getValue());
            addStmt.setString(13, System.currentTimeMillis() + "");
            addStmt.setString(14, film.getLinkWatch());
            int rs = addStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editFilm(Film film) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET id_cate = ?, title = ?, poster = ?, "
                    + "score = ?, content = ?, duration = ?, opening_day = ?, trailer = ?, director = ?, country = ?, status = ?, "
                    + "property = ?, link_watch = ? WHERE id = ? ");
            editStmt.setInt(1, film.getIdCate());
            editStmt.setString(2, film.getTitle());
            editStmt.setString(3, film.getPoster());
            editStmt.setString(4, film.getScore());
            editStmt.setString(5, film.getContent());
            editStmt.setString(6, film.getDuration());
            editStmt.setString(7, film.getOpenDay());
            editStmt.setString(8, film.getTrailer());
            editStmt.setString(9, film.getDirector());
            editStmt.setString(10, film.getCountry());
            editStmt.setInt(11, film.getStatus());
            editStmt.setInt(12, film.getProperty().getValue());
            editStmt.setString(13, film.getLinkWatch());
            editStmt.setInt(14, film.getId());
            int rs = editStmt.executeUpdate();

            CACHE.remove(film.getId());
            filmCache.refresh(film.getId());

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public int deleteFilm(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }
            Film filmById = getFilmByID(id);
            if (filmById.getId() == 0) {
                return ErrorCode.NOT_EXIST.getValue();
            }
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM `" + NAMETABLE + "` WHERE id = ?");
            deleteStmt.setInt(1, id);
            int rs = deleteStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

}
