package com.bing.water.common.model;

import java.util.List;

/**
 * Created by xuguobing on 2016/11/1 0001.
 */
public interface BaseDao<T> {

    public T get(String id);

    public T get(T entity);

    public int insert(T entity);

    public int update(T entity);

    public int delete(String id);

    public List<T> findAll();

    public int pageCount(DtGridSearch search);

    public List<T> pageList(DtGridSearch search);

}
