
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Pattern;

public class TestUrlSlug {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public String toSlug(String input) {
        input = input.replace("Đ", "D").replace("đ", "d");
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        slug = slug.replace("---", "-").replace("--", "-");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    public static void main(String[] args) {
        TestUrlSlug test = new TestUrlSlug();
        System.out.println(test.toSlug("Gia Tộc Gaunt: Hồi Ký Của Chúa Tể Voldemort - The House Of Gaunt: Lord Voldemort Origins"));
        //Lệnh Truy Nã Đỏ - Red Notice (2021)
    }
}
