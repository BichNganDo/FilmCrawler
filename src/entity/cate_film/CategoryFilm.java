package entity.cate_film;

public class CategoryFilm {

    private int id;
    private String name;
    private int status;
    private String createdDate;

    public CategoryFilm() {
    }

    public CategoryFilm(int id, String name, int status, String createdDate) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

}
