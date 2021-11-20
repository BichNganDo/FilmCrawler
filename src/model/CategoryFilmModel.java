package model;

import client.MysqlClient;
import common.ErrorCode;
import entity.cate_film.CategoryFilm;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class CategoryFilmModel {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "cate_film";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static CategoryFilmModel INSTANCE = new CategoryFilmModel();

    public List<CategoryFilm> getSliceCateFilm(int offset, int limit, String searchQuery, int searchStatus) {
        List<CategoryFilm> resultListCateFilm = new ArrayList<>();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return resultListCateFilm;

            }
            String sql = "SELECT * FROM `" + NAMETABLE
                    + "` WHERE 1 = 1";

            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND name LIKE ? ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND status = ? ";
            }

            sql = sql + " LIMIT ? OFFSET ? ";

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchStatus > 0) {
                ps.setInt(param++, searchStatus);
            }

            ps.setInt(param++, limit);
            ps.setInt(param++, offset);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CategoryFilm categoryFilm = new CategoryFilm();
                categoryFilm.setId(rs.getInt("id"));
                categoryFilm.setName(rs.getString("name"));
                categoryFilm.setStatus(rs.getInt("status"));
                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                categoryFilm.setCreatedDate(dateString);

                resultListCateFilm.add(categoryFilm);
            }

            return resultListCateFilm;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return resultListCateFilm;
    }

    public int getTotalCateFilm(String searchQuery, int searchStatus) {
        int total = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return total;
            }
            String sql = "SELECT COUNT(id) AS total FROM `" + NAMETABLE + "` WHERE 1=1";
            if (StringUtils.isNotEmpty(searchQuery)) {
                sql = sql + " AND name LIKE ? ";
            }

            if (searchStatus > 0) {
                sql = sql + " AND status = ? ";
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            int param = 1;

            if (StringUtils.isNotEmpty(searchQuery)) {
                ps.setString(param++, "%" + searchQuery + "%");
            }

            if (searchStatus > 0) {
                ps.setInt(param++, searchStatus);
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

    public CategoryFilm getCategoryFilmByID(int id) {
        CategoryFilm result = new CategoryFilm();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getNewsByIdStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE id = ? ");
            getNewsByIdStmt.setInt(1, id);

            ResultSet rs = getNewsByIdStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setName(rs.getString("name"));
                result.setStatus(rs.getInt("status"));

                long currentTimeMillis = rs.getLong("created_date");
                Date date = new Date(currentTimeMillis);
                String dateString = sdf.format(date);
                result.setCreatedDate(dateString);
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return result;
    }

    public int getIdCateByCategory(String categoryName) {
        int idCate = 0;
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return idCate;
            }
            PreparedStatement getIdCateByCategoryStmt = conn.prepareStatement("SELECT cate_film.id FROM `" + NAMETABLE + "` WHERE name = ? ");
            getIdCateByCategoryStmt.setString(1, categoryName);

            ResultSet rs = getIdCateByCategoryStmt.executeQuery();

            if (rs.next()) {
                idCate = rs.getInt("id");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        if (idCate > 0) {
            return idCate;
        }

        idCate = INSTANCE.addCategoryFlm(categoryName, 1);

        return idCate;

    }

    public int addCategoryFlm(String name, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement addStmt = conn.prepareStatement("INSERT INTO `" + NAMETABLE + "` (name, status, created_date)"
                    + "VALUES (?, ?, ?)", new String[]{"id"});
            addStmt.setString(1, name);
            addStmt.setInt(2, status);
            addStmt.setString(3, System.currentTimeMillis() + "");
            int result = addStmt.executeUpdate();
            if (result > 0) {
                try (ResultSet rs = addStmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        result = rs.getInt(1);
                    }
                    rs.close();
                }
            }

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }

        return ErrorCode.FAIL.getValue();
    }

    public int editCategoryFilm(int id, String name, int status) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            PreparedStatement editStmt = conn.prepareStatement("UPDATE `" + NAMETABLE + "` SET name = ?, status = ? WHERE id = ?");
            editStmt.setString(1, name);
            editStmt.setInt(2, status);
            editStmt.setInt(3, id);

            int rs = editStmt.executeUpdate();

            return rs;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            dbClient.releaseDbConnection(conn);
        }
        return ErrorCode.FAIL.getValue();
    }

    public int deleteCategoryFilm(int id) {
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return ErrorCode.CONNECTION_FAIL.getValue();
            }

            CategoryFilm cateFilmById = getCategoryFilmByID(id);
            if (cateFilmById.getId() == 0) {
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
