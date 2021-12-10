package client;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author root
 */
public class MysqlClient {

    private final String _host;
    private final int _port;
    private final String _dbname;
    private final String _user;
    private final String _password;
    private final int _poolsize;
    private BlockingQueue<Connection> pool;
    private String url;
    private static final Map<String, MysqlClient> _cli = new ConcurrentHashMap<String, MysqlClient>();

    private MysqlClient() {
//        _host = "http://192.168.1.199";
        _host = "localhost";
        _port = 3306;
        _dbname = "movie_project";
        _user = "root";
//        _password = "";
        _password = "Aoanhs2303!@#";
        _poolsize = 10;
        this.init();
    }

    public static MysqlClient getMysqlCli() {
        return new MysqlClient();
    }

    private boolean init() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://" + _host + ":" + _port + "/" + _dbname + "?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&interactiveClient=true" + "&user=" + _user + "&password=" + _password;
            BlockingQueue<Connection> cnnPool = new ArrayBlockingQueue<Connection>(_poolsize);
            while (cnnPool.size() < _poolsize) {
                cnnPool.add(DriverManager.getConnection(url));
            }
            pool = cnnPool;
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public Connection getDbConnection() {
        Connection conn = null;
        int retry = 0;
        do {
            try {
                conn = pool.poll(10000, TimeUnit.MILLISECONDS);
                if (conn == null || !conn.isValid(0)) {
                    conn = DriverManager.getConnection(url);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
            ++retry;
        } while (conn == null && retry < 3);
        return conn;
    }

    public void releaseDbConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!this.pool.offer(conn)) {
                    conn.close();
                }
            } catch (Exception ex) {
            }
        }
    }

}
