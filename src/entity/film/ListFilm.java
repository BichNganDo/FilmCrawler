package entity.film;

import java.util.List;

public class ListFilm {

    private int total;
    private List<Film> listFilm;
    private int itemPerPage;

    public ListFilm() {
    }

    public ListFilm(int total, List<Film> listFilm, int itemPerPage) {
        this.total = total;
        this.listFilm = listFilm;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Film> getListFilm() {
        return listFilm;
    }

    public void setListFilm(List<Film> listFilm) {
        this.listFilm = listFilm;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
