package templater;

import freemarker.template.Template;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

public class PageGenerator {

    public static final String HTML_DIR = "templates";
    public static final String UTF8 = "UTF-8";

    private static PageGenerator pageGenerator;
    private Configuration cfg;

    public static PageGenerator instance() {
        if (pageGenerator == null) {
            pageGenerator = new PageGenerator();
        }

        return pageGenerator;
    }

    public String getPage(String filename, Map<String, Object> data) {
        Writer stream = new StringWriter();
        try {
            Template template = cfg.getTemplate(HTML_DIR + File.separator + filename, UTF8);
            template.process(data, stream);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        return stream.toString();
    }

    private PageGenerator() {
        cfg = new Configuration();
    }

}
