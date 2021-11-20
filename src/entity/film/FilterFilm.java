package entity.film;

public class FilterFilm {

    private int offset;
    private int limit;
    private String searchQuery;
    private int searchCate;
    private int searchProperty;
    private int searchStatus;

    public FilterFilm() {
    }

    public FilterFilm(int offset, int limit, String searchQuery, int searchCate, int searchProperty, int searchStatus) {
        this.offset = offset;
        this.limit = limit;
        this.searchQuery = searchQuery;
        this.searchCate = searchCate;
        this.searchProperty = searchProperty;
        this.searchStatus = searchStatus;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public int getSearchCate() {
        return searchCate;
    }

    public void setSearchCate(int searchCate) {
        this.searchCate = searchCate;
    }

    public int getSearchProperty() {
        return searchProperty;
    }

    public void setSearchProperty(int searchProperty) {
        this.searchProperty = searchProperty;
    }

    public int getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(int searchStatus) {
        this.searchStatus = searchStatus;
    }

}
