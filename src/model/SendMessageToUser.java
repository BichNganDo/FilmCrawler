package model;

import entity.film.Film;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class SendMessageToUser {

    public static SendMessageToUser INSTANCE = new SendMessageToUser();

    private SendMessageToUser() {
    }

    public int sendMessageToUser(String userId, Film film) throws UnsupportedEncodingException, IOException {
        String postUrl = "https://openapi.zalo.me/v2.0/oa/message";// put in your url 
        JSONObject jsonData = new JSONObject();
        JSONObject recipient = new JSONObject();
        JSONObject messageObj = new JSONObject();
        JSONObject attachmentObj = new JSONObject();
        attachmentObj.put("type", "template");
        JSONObject payloadObj = new JSONObject();
        payloadObj.put("template_type", "list");

        JSONArray elementArray = new JSONArray();

        JSONObject elementObj = new JSONObject();
        elementObj.put("title", film.getTitle());
        elementObj.put("subtitle", film.getContent().substring(0, 151));
        elementObj.put("image_url", "https://designer.com.vn/wp-content/uploads/2017/07/poster-phim-kinh-di.jpg");
        JSONObject defaultObj = new JSONObject();
        defaultObj.put("type", "oa.open.url");
        defaultObj.put("url", film.getLinkWatch());
        elementObj.put("default_action", defaultObj);
        elementArray.put(elementObj);

        JSONObject elementObj2 = new JSONObject();
        elementObj2.put("title", "Trailer phim");
        elementObj2.put("image_url", "https://www.freeiconspng.com/uploads/play-button-icon-png-17.png");
        JSONObject defaultObj2 = new JSONObject();
        defaultObj2.put("type", "oa.open.url");
        defaultObj2.put("url", film.getTrailer());
        elementObj2.put("default_action", defaultObj2);
        elementArray.put(elementObj2);

        JSONObject elementObj3 = new JSONObject();
        elementObj3.put("title", "Xem phim");
        elementObj3.put("image_url", "https://static.vecteezy.com/system/resources/previews/001/186/943/non_2x/green-play-button-png.png");
        JSONObject defaultObj3 = new JSONObject();
        defaultObj3.put("type", "oa.open.url");
        defaultObj3.put("url", film.getLinkWatch());
        elementObj3.put("default_action", defaultObj3);
        elementArray.put(elementObj3);

        payloadObj.put("elements", elementArray);
        attachmentObj.put("payload", payloadObj);
        messageObj.put("attachment", attachmentObj);
        messageObj.put("text", "Thành công");

        recipient.put("user_id", userId);
        jsonData.put("recipient", recipient);
        jsonData.put("message", messageObj);

        String databody = jsonData.toString();
        System.out.println(databody);
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(postUrl);
        StringEntity postingString = new StringEntity(databody, "UTF-8");
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        post.setHeader("access_token", "3WXw7cyFPdC12NOPRmXdTmG7T1bb7pHq1MDMKsKw5c5-L0jrVmfC76Xq8ZjG1bGhN5SY4NCGRXj87dmMSsCFU6WXQXD8H3SwOHfRDdjEFWzaB5Oa24aQ8oSoT1WvRmeEEGbf62XdE2W20rLOOcD9DNKzL19DP143K3O32qfpGJjx2Iq5JmXzQt5sRsOH0WLy0bKOTHyHPcPoS2jQ1Yn_SZPP11mQ1tCk5m5k4XPF9XSNDqWT1MDLS0O596S-1bCF3b8bAWC3Gna9U54s1ma06He84nuKSNaXB2GdD3XALGubDnaQC5as95ra46rm");

        HttpResponse response = httpClient.execute(post);
        System.out.println(response);

        HttpEntity entity = response.getEntity();

        if (entity != null) {
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println("Response body: " + responseString);
        }

        return 0;
    }
}
