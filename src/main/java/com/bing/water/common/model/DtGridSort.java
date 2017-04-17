package com.bing.water.common.model;

import java.io.Serializable;

/**
 * Created by xuguobing
 */
public class DtGridSort implements Serializable {

    public static final String ASC = "1";
    public static final String DESC = "2";
    private String sortColumn;
    private String sortType; //排序类别 [0-不排序，1-正序，2-倒序]

    public DtGridSort() {
    }

    public DtGridSort(String sortColumn) {
        this.sortColumn = sortColumn;
        this.sortType = ASC;
    }

    public DtGridSort(String sortColumn, String sortType) {
        this.sortColumn = sortColumn;
        this.sortType = sortType;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
