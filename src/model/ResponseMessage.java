package model;

import client.MysqlClient;
import entity.film.Film;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ResponseMessage {

    private static final MysqlClient dbClient = MysqlClient.getMysqlCli();
    private final String NAMETABLE = "film";
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static ResponseMessage INSTANCE = new ResponseMessage();

    public Film getFilmByTitle(String title) {
        Film result = new Film();
        Connection conn = null;
        try {
            conn = dbClient.getDbConnection();
            if (null == conn) {
                return result;
            }
            PreparedStatement getKeyWordByKeyStmt = conn.prepareStatement("SELECT * FROM `" + NAMETABLE + "` WHERE title = ? ");
            getKeyWordByKeyStmt.setString(1, title);

            ResultSet rs = getKeyWordByKeyStmt.executeQuery();

            if (rs.next()) {
                result.setId(rs.getInt("id"));
                result.setTitle(rs.getString("title"));
                result.setPosterUrlWithBaseDomain(rs.getString("poster"));
                result.setContent(rs.getString("content"));
                result.setDuration(rs.getString("duration"));
                result.setOpenDay(rs.getString("opening_day"));
                result.setTrailer(rs.getString("trailer"));
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

}
