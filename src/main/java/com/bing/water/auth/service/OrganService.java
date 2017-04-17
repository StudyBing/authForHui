/**
 *
 */
package com.bing.water.auth.service;

import com.bing.water.auth.dao.OrganDao;
import com.bing.water.auth.dao.RoleDao;
import com.bing.water.auth.entity.Organ;
import com.bing.water.auth.entity.Role;
import com.bing.water.auth.entity.User;
import com.bing.water.common.model.AjaxReturn;
import com.bing.water.common.model.BaseEntity;
import com.bing.water.common.model.DtGridData;
import com.bing.water.common.model.DtGridSearch;
import com.bing.water.common.utils.IdGen;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 机构Service
 *
 * @author xuguobing
 */
@Service
@Transactional(readOnly = true)
public class OrganService {

    private Logger logger = LoggerFactory.getLogger(OrganService.class);

    @Autowired
    private OrganDao organDao;

    @Autowired
    private RoleDao roleDao;


    public DtGridData<Organ> findPage(DtGridSearch search) {
        Integer count = organDao.pageCount(search);
        List<Organ> list = null;
        if (count > 0) {
            list = organDao.pageList(search);
        }
        return new DtGridData<Organ>(list, count, search);
    }

    public Organ findById(String id) {
        return organDao.get(id);
    }

    @Transactional(readOnly = false)
    public AjaxReturn<Map<String, String>> saveOrUpdate(Organ vo, User user) {
        if (vo != null && StringUtils.isNotBlank(vo.getId())) { //修改
            Organ po = organDao.get(vo.getId());
            if (po != null) {
                BeanUtils.copyProperties(vo, po, BaseEntity.IGNORES);
                po.initByUpdate(user);
                organDao.update(po);
                return new AjaxReturn<Map<String, String>>(true, "修改成功");
            } else {
                return new AjaxReturn<Map<String, String>>(false, "传入ID无法找到记录");
            }
        } else { //新增
            vo.setId(IdGen.uuid());
            vo.init(user);
            organDao.insert(vo);
            return new AjaxReturn<Map<String, String>>(true, "保存成功");
        }
    }

    @Transactional(readOnly = false)
    public void deleteById(String id) {
        organDao.delete(id);
    }

    public List<Organ> findAll() {
        return organDao.findAll();
    }

    public Organ findByOrgCode(String orgCode) {
        return organDao.findByOrgCode(orgCode);
    }

    public Organ findByName(String name) {
        List<Organ> organList = organDao.findByName(name);
        if (organList != null && organList.size() > 0) {
            if (organList.size() > 1) {
                logger.error("Organ表中存在重复，重复的机构名称为{}", name);
            }
            return organList.get(0);
        }
        return null;
    }

    public List<Organ> findOrgansByPermission(String userId, String permission) {
        if (userId == null || "".equals(userId.trim())) {
            return null;
        }
        if (permission == null || "".equals(permission.trim())) {
            return null;
        }
        if ("1".equals(userId)) {
            return organDao.findAll();
        } else {
            List<Role> roles = roleDao.findRolesByPermission(userId, permission);
            if (roles == null || roles.size() == 0) {
                return null;
            }
            List<Organ> organList = organDao.findOrgansByRoles(roles);
            if (organList == null) {
                organList = new ArrayList<Organ>();
            }
            for (Role role : roles) {
                if ("1".equals(role.getDataScope())) {
                    Organ oragn = organDao.findByUserId(userId);
                    organList.add(oragn);
                    break;
                }
            }
            return organList;
        }
    }

}