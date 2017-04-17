package com.bing.water.gen.dao;

import com.bing.water.gen.model.DbColumn;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by xuguobing on 2016/11/1 0001.
 */
public interface SchemaDao {

    public List<DbColumn> findColumns(@Param("schema") String schema, @Param("tableName") String tableName);
}
