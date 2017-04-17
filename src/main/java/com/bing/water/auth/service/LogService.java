/**
 *
 */
package com.bing.water.auth.service;

import com.bing.water.auth.dao.LogDao;
import com.bing.water.auth.entity.Log;
import com.bing.water.common.model.*;
import com.bing.water.common.utils.IdGen;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 日志Service
 *
 * @author xuguobing
 */
@Service
@Transactional(readOnly = true)
public class LogService {

    @Autowired
    private LogDao logDao;

    public DtGridData<Log> findPage(DtGridSearch search) {
        if (search != null && search.getSorts() != null && search.getSorts().size() > 0) {
            for (DtGridSort sort : search.getSorts()) {
                if ("createDateStr".equals(sort.getSortColumn())) {
                    sort.setSortColumn("createDate");
                } else if ("runTimeStr".equals(sort.getSortColumn())) {
                    sort.setSortColumn("runTime");
                }
            }
        }

        Integer count = logDao.pageCount(search);
        List<Log> list = null;
        if (count > 0) {
            list = logDao.pageList(search);
        }
        return new DtGridData<Log>(list, count, search);
    }

    public Log findById(String id) {
        return logDao.get(id);
    }

    @Transactional(readOnly = false)
    public void insert(Log log) {
        if (StringUtils.isBlank(log.getId())) {
            log.setId(IdGen.uuid());
        }
        logDao.insert(log);
    }

}