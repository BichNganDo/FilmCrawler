package service;

import entity.film.Film;
import entity.info_film.InfoFilm;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.CategoryFilmModel;
import model.FilmModel;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlerPageFilm {

    private HashSet<String> links;

    public CrawlerPageFilm() {
        links = new HashSet<>();
    }

    public List<InfoFilm> getInfoMovie(String URL) {
        List<InfoFilm> listInfoFilm = new ArrayList<>();
        if (!links.contains(URL)) {
            try {
                Document document = Jsoup.connect(URL).get();

                Element listFilms = document.getElementsByClass("item-list-wrapper w-dyn-list").first();
                if (listFilms != null) {

                    Element listFilm = listFilms.getElementsByClass("item-list w-dyn-items w-row").first();
                    if (listFilm != null) {
                        Elements films = listFilm.getElementsByClass("item mobile-half w-dyn-item w-col w-col-3");

                        if (films != null && films.size() >= 1) {
                            for (Element film : films) {
                                InfoFilm infoFilm = new InfoFilm();
                                String titleFilm = film.getElementsByClass("item-block-title").first().text();
                                String categoryFilm = film.getElementsByClass("info-title-link w-inline-block").first().text();
                                String score = film.getElementsByClass("item-number").first().text().replace(",", ".");
                                String linkPoster = film.getElementsByClass("item-block").first().getElementsByTag("a").first().absUrl("style");
                                String image = linkPoster.replace("background-image:url(\"", "https:").replace("\")", "");

                                Element links = film.getElementsByClass("item-block").first();
                                String linkFilm = links.getElementsByTag("a").first().absUrl("href");

                                crawlerFilmSingle(linkFilm, infoFilm);

                                infoFilm.setTitle(titleFilm);
                                infoFilm.setImage(image);
                                infoFilm.setScore(score);
                                infoFilm.setCategory(categoryFilm);

                                listInfoFilm.add(infoFilm);
                            }
                        }
                    }
                }
                return listInfoFilm;
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return listInfoFilm;
    }

    public void crawlerFilmSingle(String urlFilm, InfoFilm infoFilm) {

        if (!links.contains(urlFilm)) {
            try {
                Document document = Jsoup.connect(urlFilm).get();
                Element film = document.getElementsByClass("header-content-block").first();
                if (film != null) {
                    Element infoDetailFilm = film.getElementsByClass("header-short-description w-richtext").first();
                    if (infoDetailFilm != null) {
                        String infoFilmSingle = film.getElementsByTag("p").first().html().replace("&nbsp;", " ");
                        String[] ele = infoFilmSingle.split("<br>");
                        for (String el : ele) {
                            String[] e = el.split(": ");
                            switch (e[0]) {
                                case "Thời gian":
                                    String duration = e[e.length - 1];
                                    if (StringUtils.isNotEmpty(duration)) {
                                        infoFilm.setDuration(duration);
                                    } else {
                                        infoFilm.setDuration("Chưa có thông tin");
                                    }

                                    break;
                                case "Đạo diễn":
                                    String director = e[e.length - 1];
                                    infoFilm.setDirector(director);
                                    break;
                                case "Quốc gia":
                                    String country = e[e.length - 1];
                                    infoFilm.setCountry(country);
                                    break;
                                case "Phát hành":
                                    String openDay = e[e.length - 1];
                                    if (StringUtils.isNotEmpty(openDay)) {
                                        infoFilm.setOpenDay(openDay);
                                    } else {
                                        infoFilm.setOpenDay("Coming soon");
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }

                Element classContent = document.getElementsByClass("rtb w-richtext").first();
                if (classContent != null) {
                    Elements getcontent = classContent.getElementsByTag("p");
                    StringBuilder content = new StringBuilder();
                    for (Element element : getcontent) {
                        content.append(element.text());
                    }

                    infoFilm.setContent(content.toString());
                }

                Element idTrailer = document.getElementById("trailer");
                if (idTrailer != null) {
                    String encodingTrailer = idTrailer.getElementsByClass("embedly-embed").first().absUrl("src");
                    String decodingTrailer = java.net.URLDecoder.decode(encodingTrailer, StandardCharsets.UTF_8.name());
                    Matcher matcher = Pattern.compile("\\?v=(.*?)&").matcher(decodingTrailer);
                    while (matcher.find()) {
                        String linkTrailer = "https://www.youtube.com/watch?v=" + matcher.group(1);
                        infoFilm.setTrailer(linkTrailer);
                    }
                }

                Element classWatchFilm = document.getElementsByClass("header-info-block").first();
                if (classWatchFilm != null) {
                    String linkWatch = classWatchFilm.getElementsByClass("button_xemphim w-button").first().absUrl("href");
                    infoFilm.setLinkWatch(linkWatch);
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void downloadImage(String src_image, String name, String dir) {
        try {
            URL url = new URL(src_image);
            URLConnection hc = url.openConnection();
            hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            hc.connect();
            InputStream in = hc.getInputStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(dir + "\\" + name));
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
        CrawlerPageFilm crawler = new CrawlerPageFilm();
        List<InfoFilm> listMovieCrawler = crawler.getInfoMovie("https://www.ssphim.net/the-loai/phim-chieu-rap");
        Collections.reverse(listMovieCrawler);
        for (InfoFilm infoFilm : listMovieCrawler) {
            Film film = new Film();

            String[] linkImage = infoFilm.getImage().split("/");
            String nameImage = linkImage[linkImage.length - 1];
            String dir = "C:\\Users\\Ngan Do\\Documents\\NetBeansProjects\\FilmCrawler\\upload\\poster_film";
            crawler.downloadImage(infoFilm.getImage(), nameImage, dir);
            film.setPoster("upload/poster_film/" + nameImage);

            int idCate = CategoryFilmModel.INSTANCE.getIdCateByCategory(infoFilm.getCategory());
            film.setIdCate(idCate);
            film.setCategory(infoFilm.getCategory());
            film.setTitle(infoFilm.getTitle());
            film.setContent(infoFilm.getContent());

            if (StringUtils.isNotEmpty(infoFilm.getDuration())) {
                film.setDuration(infoFilm.getDuration());
            } else {
                film.setDuration("Chưa có thông tin");
            }

            if (StringUtils.isNotEmpty(infoFilm.getOpenDay())) {
                film.setOpenDay(infoFilm.getOpenDay());
            } else {
                film.setOpenDay("Coming soon");
            }

            film.setTrailer(infoFilm.getTrailer());
            film.setProperty(1);
            film.setStatus(1);

            if (StringUtils.isNotEmpty(infoFilm.getDirector())) {
                film.setDirector(infoFilm.getDirector());
            } else {
                film.setDirector("Chưa có thông tin");
            }

            if (StringUtils.isNotEmpty(infoFilm.getCountry())) {
                film.setCountry(infoFilm.getCountry());
            } else {
                film.setCountry("Chưa có thông tin");
            }

            film.setScore((infoFilm.getScore()));
            film.setLinkWatch(infoFilm.getLinkWatch());
            boolean isExistFilm = FilmModel.INSTANCE.isExistFilm("upload/poster_film/" + nameImage);
            if (isExistFilm == false) {
                FilmModel.INSTANCE.addFilm(film);
            }

        }

    }

}
