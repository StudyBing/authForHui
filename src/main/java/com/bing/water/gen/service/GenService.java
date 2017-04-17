package com.bing.water.gen.service;

import com.bing.water.common.utils.FileUtils;
import com.bing.water.gen.dao.SchemaDao;
import com.bing.water.gen.model.DbColumn;
import com.bing.water.gen.model.GenTemplate;
import com.bing.water.gen.utils.FreeMarkers;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by xuguobing on 2016/11/1 0001.
 */
@Service
public class GenService {

    @Autowired
    private SchemaDao schemaDao;

    @Transactional(readOnly = true)
    public List<DbColumn> findColumns(String schema, String tableName) {
        return schemaDao.findColumns(schema, tableName);
    }


}
