package entity.film;

import common.Config;

public class Film {

    private int id;
    private int idCate;
    private String category;
    private String title;
    private String poster;
    private String content;
    private String duration;
    private String openDay;
    private String trailer;
    private int status;
    private Property property;
    private String createdDate;
    private String country;
    private String director;
    private String score;
    private String linkWatch;

    public Film() {
    }

    public Film(int id, int idCate, String category, String title, String poster, String content, String duration, String openDay, String trailer, int status, Property property, String createdDate, String country, String director, String score) {
        this.id = id;
        this.idCate = idCate;
        this.category = category;
        this.title = title;
        this.poster = poster;
        this.content = content;
        this.duration = duration;
        this.openDay = openDay;
        this.trailer = trailer;
        this.status = status;
        this.property = property;
        this.createdDate = createdDate;
        this.country = country;
        this.director = director;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCate() {
        return idCate;
    }

    public void setIdCate(int idCate) {
        this.idCate = idCate;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setPosterUrlWithBaseDomain(String poster) { // set de hien thi
        if (poster.startsWith("http")) {
            this.poster = poster;
        } else {
            this.poster = Config.APP_DOMAIN + "/" + poster;
        }
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public void setProperty(int numberProperty) {
        this.property = new Property(numberProperty);
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public class Property {

        private boolean trending;
        private boolean upcoming;
        private boolean popular;

        public Property(int numberProperty) {
            if ((numberProperty & 1) > 0) {
                trending = true;
            }
            if ((numberProperty & 2) > 0) {
                upcoming = true;
            }
            if ((numberProperty & 4) > 0) {
                popular = true;
            }

        }

        public Property(boolean trending, boolean upcoming, boolean popular) {
            this.trending = trending;
            this.upcoming = upcoming;
            this.popular = popular;
        }

        public int getValue() {
            int property = 0;
            if (this.trending) {
                property = property + 1;
            }
            if (this.upcoming) {
                property = property + 2;
            }
            if (this.popular) {
                property = property + 4;
            }
            return property;
        }

        public boolean isTrending() {
            return trending;
        }

        public void setTrending(boolean trending) {
            this.trending = trending;
        }

        public boolean isUpcoming() {
            return upcoming;
        }

        public void setUpcoming(boolean upcoming) {
            this.upcoming = upcoming;
        }

        public boolean isPopular() {
            return popular;
        }

        public void setPopular(boolean popular) {
            this.popular = popular;
        }

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
