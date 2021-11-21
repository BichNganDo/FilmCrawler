package entity.info_film;

public class InfoFilm {

    private int id;
    private String title;
    private String image;
    private String score;
    private String category;
    private String content;
    private String duration;
    private String openDay;
    private String trailer;
    private String country;
    private String director;
    private String linkWatch;

    public InfoFilm() {
    }

    public InfoFilm(int id, String title, String image, String score, String category, String content, String duration, String openDay, String trailer, String country, String director) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.score = score;
        this.category = category;
        this.content = content;
        this.duration = duration;
        this.openDay = openDay;
        this.trailer = trailer;
        this.country = country;
        this.director = director;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getOpenDay() {
        return openDay;
    }

    public void setOpenDay(String openDay) {
        this.openDay = openDay;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getLinkWatch() {
        return linkWatch;
    }

    public void setLinkWatch(String linkWatch) {
        this.linkWatch = linkWatch;
    }

}
