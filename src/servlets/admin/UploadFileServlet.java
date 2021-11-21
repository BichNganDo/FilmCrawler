package servlets.admin;

import common.Config;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.eclipse.jetty.server.Request;
import templater.PageGenerator;

//@WebServlet("admin/upload-file")
//@MultipartConfiguration(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
//        maxFileSize = 1024 * 1024 * 10, // 10MB
//        maxRequestSize = 1024 * 1024 * 50) // 50MB
public class UploadFileServlet extends HttpServlet {

    private static final MultipartConfigElement MULTI_PART_CONFIG = new MultipartConfigElement("upload");

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("app_domain", Config.APP_DOMAIN);
        pageVariables.put("static_domain", Config.STATIC_DOMAIN);

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().println(PageGenerator.instance().getPage("admin/TestUploadFile.html", pageVariables));

        response.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String contentType = request.getContentType();
        if (contentType != null && contentType.startsWith("multipart/")) {
            request.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, MULTI_PART_CONFIG);
            for (Part part : request.getParts()) {
                if ("file2".equals(part.getName())) {
                    part.write(extractFileName(part));
                }

            }
        }
    }

    private String extractFileName(Part part) {
        // form-data; name="file"; filename="C:\file1.zip"
        // form-data; name="file"; filename="C:\Note\file2.zip"
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                // C:\file1.zip
                // C:\Note\file2.zip
                String clientFileName = s.substring(s.indexOf("=") + 2, s.length() - 1);
                clientFileName = clientFileName.replace("\\", "/");
                int i = clientFileName.lastIndexOf('/');
                // file1.zip
                // file2.zip
                return clientFileName.substring(i + 1);
            }
        }
        return null;
    }
}
