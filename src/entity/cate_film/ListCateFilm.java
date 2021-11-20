package entity.cate_film;

import entity.cate_film.CategoryFilm;
import java.util.List;

public class ListCateFilm {

    private int total;
    private List<CategoryFilm> listCateFilm;
    private int itemPerPage;

    public ListCateFilm() {
    }

    public ListCateFilm(int total, List<CategoryFilm> listCateFilm, int itemPerPage) {
        this.total = total;
        this.listCateFilm = listCateFilm;
        this.itemPerPage = itemPerPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<CategoryFilm> getListCateFilm() {
        return listCateFilm;
    }

    public void setListCateFilm(List<CategoryFilm> listCateFilm) {
        this.listCateFilm = listCateFilm;
    }

    public int getItemPerPage() {
        return itemPerPage;
    }

    public void setItemPerPage(int itemPerPage) {
        this.itemPerPage = itemPerPage;
    }

}
