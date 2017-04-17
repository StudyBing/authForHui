/**
 *
 */
package com.bing.water.auth.service;

import com.bing.water.auth.dao.RoleDao;
import com.bing.water.auth.entity.Role;
import com.bing.water.auth.entity.RoleMenu;
import com.bing.water.auth.entity.RoleOrgan;
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

import java.util.List;
import java.util.Map;

/**
 * 角色Service
 *
 * @author xuguobing
 */
@Service
@Transactional(readOnly = true)
public class RoleService {

    private Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleDao roleDao;

    public DtGridData<Role> findPage(DtGridSearch search) {
        Integer count = roleDao.pageCount(search);
        List<Role> list = null;
        if (count > 0) {
            list = roleDao.pageList(search);
        }
        return new DtGridData<Role>(list, count, search);
    }

    public Role findById(String id) {
        Role role = roleDao.get(id);
        if (role != null) {
            List<String> menuIds = roleDao.findMenuIdsByRoleId(id);
            role.setMenuIds(menuIds);

            List<String> organIds = roleDao.findOrganIdsByRoleId(id);
            role.setOrganIds(organIds);
        }
        return role;
    }

    @Transactional(readOnly = false)
    public AjaxReturn<Map<String, String>> saveOrUpdate(Role vo, User user) {
        AjaxReturn<Map<String, String>> result = null;
        String roleId = null;
        if (vo != null && StringUtils.isNotBlank(vo.getId())) { //修改
            Role po = roleDao.get(vo.getId());
            if (po != null) {
                BeanUtils.copyProperties(vo, po, BaseEntity.IGNORES);
                po.initByUpdate(user);
                roleDao.update(po);
                result = new AjaxReturn<Map<String, String>>(true, "修改成功");
                roleId = po.getId();
            } else {
                result = new AjaxReturn<Map<String, String>>(false, "传入ID无法找到记录");
            }
        } else { //新增
            vo.setId(IdGen.uuid());
            vo.init(user);
            roleDao.insert(vo);
            result = new AjaxReturn<Map<String, String>>(true, "保存成功");
            roleId = vo.getId();
        }
        if (result.getOk()) {
            //保存资源关联表
            roleDao.deleteRoleMenu(roleId);
            if (vo.getMenuIds() != null && vo.getMenuIds().size() > 0) {
                for (String menuId : vo.getMenuIds()) {
                    roleDao.insertRoleMenu(new RoleMenu(roleId, menuId));
                }
            }
            //保存机构关联表
            roleDao.deleteRoleOrgan(roleId);
            if ("2".equals(vo.getDataScope()) && vo.getOrganIds() != null && vo.getOrganIds().size() > 0) {
                for (String organId : vo.getOrganIds()) {
                    roleDao.insertRoleOrgan(new RoleOrgan(roleId, organId));
                }
            }
        }
        return result;
    }

    @Transactional(readOnly = false)
    public void deleteById(String id) {
        roleDao.delete(id);
    }

    public List<Role> findRolesByUser(User user) {
        if ("1".equals(user.getId())) {
            return roleDao.findAll();
        } else {
            return roleDao.findRolesByUser(user);
        }
    }

    @Transactional(readOnly = false)
    public AjaxReturn<String> deleteRoleUser(String roleId, String userId) {
        roleDao.deleteRoleUser(roleId, userId);
        return new AjaxReturn<String>(true, "移除成功");
    }

    public Role findByName(String name) {
        List<Role> roles = roleDao.findByName(name);
        if (roles != null && roles.size() > 0) {
            if (roles.size() > 1) {
                logger.error("Role表中存在重复，重复的角色名称为{}", name);
            }
            return roles.get(0);
        }
        return null;
    }

    public List<Role> findRolesByPermission(String userId, String permission) {
        if (userId == null || "".equals(userId.trim())) {
            return null;
        }
        if (permission == null || "".equals(permission.trim())) {
            return null;
        }
        List<Role> roles = roleDao.findRolesByPermission(userId,permission);
        return roles;
    }
}