
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {

    public static void main(String[] args) {
        String line = "//cdn.embedly.com/widgets/media.html?src=https://www.youtube.com/embed/Pj0wz7zu3Ms?feature=oembed&display_name=YouTube&url=https://www.youtube.com/watch?v=Pj0wz7zu3Ms&image=https://i.ytimg.com/vi/Pj0wz7zu3Ms/hqdefault.jpg&key=96f1f04c5f4143bcb0f2e68c87d65feb&type=text/html&scaema=youtube";
        Matcher matcher = Pattern.compile("\\?v=(.*?)&").matcher(line);
        while (matcher.find()) {
            System.out.println("https://www.youtube.com/watch?v=" + matcher.group(1));
        }
    }
}
