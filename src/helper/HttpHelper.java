/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Ngan Do
 */
public class HttpHelper {

    private static Gson gson = new Gson();
    private static final String UTF8 = "UTF-8";

    public static String buildParameter(Map<String, String> data) {
        StringBuilder result = new StringBuilder(4096);

        try {
            if (data != null) {
                Set<Map.Entry<String, String>> setEntry = data.entrySet();
                for (Iterator<Map.Entry<String, String>> it = setEntry.iterator(); it.hasNext();) {
                    Map.Entry entry = it.next();
                    result.append("&");
                    result.append(URLEncoder.encode(entry.getKey().toString(), UTF8));
                    result.append("=");
                    result.append(URLEncoder.encode(entry.getValue().toString(), UTF8));
                }
            }
        } catch (UnsupportedEncodingException ex) {
        }
        if (result.indexOf("&") > -1) {
            result.deleteCharAt(0);
        }
        return result.toString();
    }

    public static class ZHttpResponse {

        private int status;
        private String link;
        private String data;
        private byte[] bytes;
        private String exception;
        private long latency;

        public ZHttpResponse() {
            this.status = -1;
            this.link = "";
            this.data = "";
            this.exception = "";

        }

        public ZHttpResponse(int status) {
            this.status = status;
            this.link = "";
            this.data = "";
            this.exception = "";
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public void setBytes(byte[] bytes) {
            this.bytes = bytes;
        }

        public String getException() {
            return exception;
        }

        public void setException(String exception) {
            this.exception = exception;
        }

        public long getLatency() {
            return latency;
        }

        public void setLatency(long latency) {
            this.latency = latency;
        }

        @Override
        public String toString() {
            String result = "";
            try {
                result = gson.toJson(this);
            } catch (Exception e) {
            }
            return result;
        }

    }

    public static ZHttpResponse sendGetRequest(String url, Map<String, String> data) {
        ZHttpResponse resp = new ZHttpResponse(HttpServletResponse.SC_NOT_FOUND);
        resp.setLink(url);

        StringBuilder result = new StringBuilder();
        try {
            String strData = buildParameter(data);
            if (!strData.isEmpty()) {
                url += "?" + strData;
            }
            URL ulrConn = new URL(url);
            URLConnection conn = ulrConn.openConnection();
            conn.setConnectTimeout(6000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            reader.close();
        } catch (Exception ex) {
            if (ex.getMessage().startsWith("Server returned HTTP response code: 500 for URL")) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else if (ex.getMessage().startsWith("Server returned HTTP response code: 502 for URL")) {
                resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            } else if (ex.getMessage().startsWith("Server returned HTTP response code: 503 for URL")) {
                resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            } else if (ex.getMessage().startsWith("Server returned HTTP response code: 504 for URL")) {
                resp.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
            } else {
                resp.setStatus(-2);
            }

            resp.setException("error");

        }
        resp.setData(result.toString());
        return resp;
    }

    public static ZHttpResponse sendRequest(String url, String method, final String data, String contentType) {
        ZHttpResponse resp = new ZHttpResponse(HttpServletResponse.SC_NOT_FOUND);
        resp.setLink(url);

        StringBuilder result = new StringBuilder(4096);
        BufferedReader reader = null;
        HttpURLConnection conn = null;
        OutputStreamWriter writer = null;
        long time = System.currentTimeMillis();
        try {
            URL ulrConn = new URL(url);
            conn = (HttpURLConnection) ulrConn.openConnection();
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            /*
			 * Post parameter
             */

            conn.setDoOutput(true);
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Content-Length", String.valueOf(data.length()));

            writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(data);
            writer.flush();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            resp.setStatus(HttpServletResponse.SC_OK);

            conn.disconnect();
            reader.close();
            writer.close();

        } catch (Exception ex) {
            if (ex.getMessage().startsWith("Server returned HTTP response code: 500 for URL")) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } else if (ex.getMessage().startsWith("Server returned HTTP response code: 502 for URL")) {
                resp.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            } else if (ex.getMessage().startsWith("Server returned HTTP response code: 503 for URL")) {
                resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            } else if (ex.getMessage().startsWith("Server returned HTTP response code: 504 for URL")) {
                resp.setStatus(HttpServletResponse.SC_GATEWAY_TIMEOUT);
            } else {
                resp.setStatus(-2);
            }

            resp.setException("error");
            if (conn != null) {
                conn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex1) {
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex1) {
                }
            }
        } finally {
            resp.setLatency(System.currentTimeMillis() - time);
        }

        resp.setData(result.toString());
        return resp;
    }

    public static String getBodyData(HttpServletRequest req) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        String ret = "";
        try {
            InputStream inputStream = req.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                char[] charBuffer = new char[1024];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
            ret = stringBuilder.toString();
        } catch (IOException ex) {
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                }
            }
        }

        return ret;

    }

}
