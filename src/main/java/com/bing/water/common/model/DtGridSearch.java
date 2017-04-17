package com.bing.water.common.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.bing.water.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuguobing
 */
public class DtGridSearch implements Serializable {

    private String dtGridPager;
    private Boolean isExport;
    private Integer pageSize = 20;
    private Integer startRecord = 0;
    private Integer nowPage = 1;
    private Integer recordCount;
    private Integer pageCount;

    private List<DtGridSort> sorts;

    public Boolean needSort() {
        return sorts != null && sorts.size() > 0;
    }

    public String getSort() {
        if (sorts == null) {
            return null;
        }
        StringBuffer sort = new StringBuffer(10);
        for (DtGridSort s : sorts) {
            if (s != null) {
                if (sort.length() > 0) {
                    sort.append(",");
                }
                if (DtGridSort.ASC.equals(s.getSortType())) {
                    sort.append(s.getSortColumn() + " asc ");
                } else if (DtGridSort.DESC.equals(s.getSortType())) {
                    sort.append(s.getSortColumn() + " desc ");
                } else {
                    sort.append(s.getSortColumn() + " ");
                }
            }
        }
        return sort.toString();
    }

    public void addSort(String column) {
        if (sorts == null) {
            sorts = Lists.newArrayList();
        }
        if (StringUtils.isNotBlank(column)) {
            sorts.add(new DtGridSort(column));
        }
    }

    public void addSort(String column, String type) {
        if (sorts == null) {
            sorts = Lists.newArrayList();
        }
        if (StringUtils.isNotBlank(column) && StringUtils.isNotBlank(type) && !"0".equals(type)) {
            sorts.add(new DtGridSort(column, type));
        }
    }

    public DtGridSearch() {
    }

    public DtGridSearch(DtGridSearch search) {
        if (search != null) {
            this.dtGridPager = search.getDtGridPager();
            this.isExport = search.getIsExport();
            this.pageSize = search.getPageSize();
            this.startRecord = search.getStartRecord();
            this.nowPage = search.getNowPage();
            this.pageCount = search.getPageCount();
            this.recordCount = search.getRecordCount();
        }
    }

    public String getDtGridPager() {
        return dtGridPager;
    }


    public void setDtGridPager(String dtGridPager) {
        if (StringUtils.isNotBlank(dtGridPager)) {
            if (dtGridPager.indexOf("&quot;") > 0) {
                dtGridPager = dtGridPager.replace("&quot;", "\"");
            } else if (dtGridPager.indexOf("&amp;quot;") > 0) {
                dtGridPager = dtGridPager.replace("&amp;quot;", "\"");
            }
        }
        Gson gson = new Gson();
        Map<String, Object> map = gson.fromJson(dtGridPager, Map.class);

        this.dtGridPager = dtGridPager;
        this.isExport = getBoolean(map.get("isExport"));
        this.pageSize = getInteger(map.get("pageSize"));
        this.startRecord = getInteger(map.get("startRecord"));
        this.nowPage = getInteger(map.get("nowPage"));
        this.recordCount = getInteger(map.get("recordCount"));

        Map<String, Object> paramters = (Map<String, Object>) map.get("parameters");
        this.addSort(getString(paramters.get("sortColumn")), getString(paramters.get("sortType")));

        for (String prop : paramters.keySet()) {
            Field field = ReflectionUtils.findField(this.getClass(), prop);
            if (field != null) {
                setFieldValue(field, paramters.get(prop));
            }
        }
    }

    protected void setFieldValue(Field field, Object v) {
        if (v != null && StringUtils.isBlank(v.toString())) {
            return;
        }
        field.setAccessible(true);
        if (field.getType().equals(Date.class)) {
            Set<String> patterns = Sets.newHashSet();
            patterns.add("yyyy-MM-dd");
            if (field.isAnnotationPresent(DateTimeFormat.class)) {
                DateTimeFormat format = field.getAnnotation(DateTimeFormat.class);
                if (format != null) {
                    patterns.add(format.pattern());
                }
            }
            Date date = null;
            try {
                date = DateUtils.parseDate(getString(v), patterns.toArray(new String[patterns.size()]));
            } catch (ParseException e) {
            }
            ReflectionUtils.setField(field, this, date);
        } else if (field.getType().equals(String.class)) {
            ReflectionUtils.setField(field, this, getString(v));
        } else if (field.getType().equals(Integer.class)) {
            ReflectionUtils.setField(field, this, getInteger(v));
        } else if (field.getType().equals(Double.class)) {
            ReflectionUtils.setField(field, this, getDouble(v));
        } else if (field.getType().equals(Float.class)) {
            ReflectionUtils.setField(field, this, getFloat(v));
        } else if (field.getType().equals(Boolean.class)) {
            ReflectionUtils.setField(field, this, getBoolean(v));
//        } else if (field.getType().equals(String.class)) {
//            ReflectionUtils.setField(field, this, getString(v));
        }
    }

    protected Float getFloat(Object o) {
        Object r = value(o);
        if (r != null) {
            return (Float) r;
        }
        return null;
    }

    protected Double getDouble(Object o) {
        Object r = value(o);
        if (r != null) {
            return (Double) r;
        }
        return null;
    }

    protected Integer getInteger(Object o) {
        Object r = value(o);
        if (r != null) {
            return (Integer) r;
        }
        return null;
    }

    protected Boolean getBoolean(Object o) {
        Object r = value(o);
        if (r != null) {
            return (Boolean) r;
        }
        return null;
    }

    protected String getString(Object o) {
        Object r = value(o);
        if (r != null) {
            return String.valueOf(r);
        }
        return null;
    }

    protected Object value(Object o) {
        if (o == null) {
            return null;
        } else if (o instanceof Double) {
            Double d = (Double) o;
            return d.intValue();
        } else {
            return o;
        }
    }


    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Boolean getIsExport() {
        return isExport;
    }

    public void setIsExport(Boolean isExport) {
        this.isExport = isExport;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartRecord() {
        return startRecord;
    }

    public void setStartRecord(Integer startRecord) {
        this.startRecord = startRecord;
    }

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
        try {
            if (recordCount != null && recordCount > 0 && pageSize != null && pageSize > 0) {
                this.pageCount = (int) Math.ceil((double) recordCount / pageSize);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<DtGridSort> getSorts() {
        return sorts;
    }

    public void setSorts(List<DtGridSort> sorts) {
        this.sorts = sorts;
    }
}
