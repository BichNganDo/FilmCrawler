package servlets.webhook;

import entity.film.Film;
import helper.HttpHelper;
import helper.ServletUtil;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ResponseMessage;
import model.SendMessageToUser;
import org.json.JSONObject;

public class Webhook extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.printJson(request, response, "GET OK");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletUtil.printJson(request, response, "OK");
        String body = HttpHelper.getBodyData(request);
        JSONObject jbody = new JSONObject(body);
        String idSender = jbody.optJSONObject("sender").optString("id");
        String textMessage = jbody.optJSONObject("message").optString("text");
        System.out.println(jbody);

        Film film = ResponseMessage.INSTANCE.getFilmByTitle(textMessage);
        SendMessageToUser.INSTANCE.sendMessageToUser(idSender, film);
    }
}
