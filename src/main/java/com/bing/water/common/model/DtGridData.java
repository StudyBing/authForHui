package com.bing.water.common.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuguobing.
 */
public class DtGridData<T> extends DtGridSearch implements Serializable {

    private List<T> exhibitDatas;

    private Boolean exportAllData;

    private String[] exportColumns;

    private List<T> exportDatas;

    private String exportFileName;

    private String exportType;

    private Boolean isSuccess;

    public DtGridData(List<T> list, Integer count, DtGridSearch search) {
        super(search);
        if(list == null){
            list = new ArrayList<T>();
        }
        this.exhibitDatas = list;
        this.isSuccess = true;
        setRecordCount(count);
    }

    public DtGridData(List<T> list, DtGridSearch search) {
        super(search);
        this.exhibitDatas = list;
        this.isSuccess = true;
        setRecordCount(list.size());
        setPageCount(1);
    }

    public List<T> getExhibitDatas() {
        return exhibitDatas;
    }

    public void setExhibitDatas(List<T> exhibitDatas) {
        this.exhibitDatas = exhibitDatas;
    }

    public Boolean getExportAllData() {
        return exportAllData;
    }

    public void setExportAllData(Boolean exportAllData) {
        this.exportAllData = exportAllData;
    }

    public String[] getExportColumns() {
        return exportColumns;
    }

    public void setExportColumns(String[] exportColumns) {
        this.exportColumns = exportColumns;
    }

    public List<T> getExportDatas() {
        return exportDatas;
    }

    public void setExportDatas(List<T> exportDatas) {
        this.exportDatas = exportDatas;
    }

    public String getExportFileName() {
        return exportFileName;
    }

    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

}
