
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class TestDownloadImage {

    public static void downloadImage(String src_image, String name, String dir) {
        try {
            URL url = new URL(src_image);
            URLConnection hc = url.openConnection();
            hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            hc.connect();
            InputStream in = hc.getInputStream();

            OutputStream out = new BufferedOutputStream(new FileOutputStream(dir + "/" + name));
            for (int b; (b = in.read()) != -1;) {
                out.write(b);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String link = "https://static.ssphim.net/static/5fe2d564b3fa6403ffa11d1c/618e3b8ad7fee447e8ebb6aa_truy-na-do.jpg";
        String[] linkImage = link.split("/");
        String nameImage = linkImage[linkImage.length - 1];
        System.out.println(nameImage);
        String dir = "image_film_download";
        downloadImage(link, nameImage, dir);
//        downloadImage(infoFilm.getImage(), nameImage, dir);
    }

}
