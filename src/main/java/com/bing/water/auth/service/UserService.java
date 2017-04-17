/**
 *
 */
package com.bing.water.auth.service;

import com.bing.water.auth.dao.UserDao;
import com.bing.water.auth.entity.RoleUser;
import com.bing.water.auth.entity.User;
import com.bing.water.common.model.AjaxReturn;
import com.bing.water.common.model.BaseEntity;
import com.bing.water.common.model.DtGridData;
import com.bing.water.common.model.DtGridSearch;
import com.bing.water.common.utils.IdGen;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;

/**
 * 用户Service
 *
 * @author xuguobing
 */
@Service
@Transactional(readOnly = true)
public class UserService {

    private static String[] IGNORES;

    static {
        List<String> list = Lists.newArrayList(BaseEntity.IGNORES);
        list.add("id");
        list.add("wdyId");
        list.add("roleIds");
        list.add("password");
        IGNORES = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            IGNORES[i] = list.get(i);
        }
    }

    @Autowired
    private UserDao userDao;

    public DtGridData<User> findPage(DtGridSearch search) {
        Integer count = userDao.pageCount(search);
        List<User> list = null;
        if (count > 0){
            list = userDao.pageList(search);
        }
        return new DtGridData<User>(list, count, search);
    }

    public User findById(String id) {
        User user = userDao.get(id);
        if (user != null) {
            List<String> roleIds = userDao.findRoleIdsByUserId(id);
            user.setRoleIds(roleIds);
        }
        return user;
    }

    @Transactional(readOnly = false)
    public AjaxReturn<Map<String, String>> saveOrUpdate(User vo, User user) {
        AjaxReturn<Map<String, String>> result = null;
        String userId = null;
        String password = null;
        String wdyId = null;
        if (vo != null && StringUtils.isNotBlank(vo.getId())) { //修改
            User po = userDao.get(vo.getId());
            if (po != null) {
                if (StringUtils.isNotBlank(vo.getPassword())) {
                    password = DigestUtils.md5DigestAsHex(vo.getPassword().getBytes());
                } else {
                    password = po.getPassword();
                }
                BeanUtils.copyProperties(vo, po, IGNORES);
                po.initByUpdate(user);
                po.setPassword(password);
                userDao.update(po);
                userId = po.getId();
                result = new AjaxReturn<Map<String, String>>(true, "修改成功");
            } else {
                result = new AjaxReturn<Map<String, String>>(false, "传入ID无法找到记录");
            }
        } else { //新增
            vo.setId(IdGen.uuid());
            vo.init(user);
            password = DigestUtils.md5DigestAsHex(vo.getPassword().getBytes());
            vo.setPassword(password);
            userDao.insert(vo);
            userId = vo.getId();
            result = new AjaxReturn<Map<String, String>>(true, "保存成功");
        }
        if (result.getOk()) {
            userDao.deleteRoleUser(userId);
            if (vo.getRoleIds() != null && vo.getRoleIds().size() > 0) {
                for (String roleId : vo.getRoleIds()) {
                    userDao.insertRoleUser(new RoleUser(userId, roleId));
                }
            }
        }
        return result;
    }

    @Transactional(readOnly = false)
    public void deleteById(String id) {
        userDao.delete(id);
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User findByOrgCodeAndJobNo(String orgCode, String jobNo) {
        return userDao.findByOrgCodeAndJobNo(orgCode, jobNo);
    }

    @Transactional(readOnly = false)
    public void changePassword(String userId, String newPw) {
        userDao.changePassword(userId, newPw);
    }

    @Transactional(readOnly = false)
    public AjaxReturn<String> saveRoleUsers(String roleId, String userIds) {
        if (StringUtils.isBlank(roleId) || StringUtils.isBlank(userIds)) {
            return new AjaxReturn<String>(false, "参数错误");
        }

        String[] uids = userIds.split(",");
        for (String userId : uids) {
            RoleUser user = new RoleUser(userId,roleId);
            userDao.insertRoleUser(user);
        }
        return new AjaxReturn<String>(true,"保存成功");
    }

    public List<User> findByOrgCode(String orgCode) {
        return userDao.findByOrgCode(orgCode);
    }

}