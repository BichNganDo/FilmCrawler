package helper;

import java.io.PrintWriter;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtil {

    private static final SimpleDateFormat RFC_822_DATE_FORMAT = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");

    public static void printJson(HttpServletRequest req, HttpServletResponse resp, String content) {
        RFC_822_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
        final String currentDate = RFC_822_DATE_FORMAT.format(new Date(System.currentTimeMillis()));
        resp.setHeader("Date", currentDate);
        resp.setContentType("application/json; charset=utf-8");

        setCrossDomainAllow(resp);

        print(resp, content);
    }

    private static void setCrossDomainAllow(HttpServletResponse resp) {
        resp.setHeader("Access-Control-Allow-Credentials", "true");
        resp.setHeader("Access-Control-Allow-Headers", "Origin, Accept, x-auth-token, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, x-requested-with");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS");
        resp.setHeader("Access-Control-Allow-Origin", "*");
    }

    public static void print(HttpServletResponse resp, String content) {
        PrintWriter out = null;
        try {
            RFC_822_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
            final String currentDate = RFC_822_DATE_FORMAT.format(new Date(System.currentTimeMillis()));
            resp.setHeader("Date", currentDate);
            resp.setCharacterEncoding("utf-8");
            //setCrossDomainAllow(resp);
            out = resp.getWriter();
            out.print(content);
        } catch (Throwable ex) {

        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    //UrlSlug
    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String toSlug(String titleFilm) {
        titleFilm = titleFilm.replace("Đ", "D").replace("đ", "d");
        String nowhitespace = WHITESPACE.matcher(titleFilm).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = slug.replace("---", "-").replace("--", "-");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
