/**
 *
 */
package com.bing.water.auth.service;

import com.bing.water.auth.dao.DictDao;
import com.bing.water.auth.entity.Dict;
import com.bing.water.auth.entity.User;
import com.bing.water.common.model.AjaxReturn;
import com.bing.water.common.model.BaseEntity;
import com.bing.water.common.model.DtGridData;
import com.bing.water.common.model.DtGridSearch;
import com.bing.water.common.utils.IdGen;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 字典Service
 *
 * @author xuguobing
 */
@Service
@Transactional(readOnly = true)
public class DictService {

    @Autowired
    private DictDao dictDao;

    public DtGridData<Dict> findPage(DtGridSearch search) {
        Integer count = dictDao.pageCount(search);
        List<Dict> list = null;
        if (count > 0) {
            list = dictDao.pageList(search);
        }
        return new DtGridData<Dict>(list, count, search);
    }

    public Dict findById(String id) {
        return dictDao.get(id);
    }

    @Transactional(readOnly = false)
    public AjaxReturn<Map<String, String>> saveOrUpdate(Dict vo, User user) {
        if (vo != null && StringUtils.isNotBlank(vo.getId())) { //修改
            Dict po = dictDao.get(vo.getId());
            if (po != null) {
                BeanUtils.copyProperties(vo, po, BaseEntity.IGNORES);
                po.initByUpdate(user);
                dictDao.update(po);
                return new AjaxReturn<Map<String, String>>(true, "修改成功");
            } else {
                return new AjaxReturn<Map<String, String>>(false, "传入ID无法找到记录");
            }
        } else { //新增
            vo.setId(IdGen.uuid());
            vo.init(user);
            dictDao.insert(vo);
            return new AjaxReturn<Map<String, String>>(true, "保存成功");
        }
    }

    @Transactional(readOnly = false)
    public void deleteById(String id) {
        dictDao.delete(id);
    }

    public List<String> findGroupType() {
        return dictDao.findGroupType();
    }
}