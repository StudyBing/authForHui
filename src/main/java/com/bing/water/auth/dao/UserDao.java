/**
 *
 */
package com.bing.water.auth.dao;

import com.bing.water.auth.entity.RoleUser;
import com.bing.water.auth.entity.User;
import com.bing.water.common.model.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户DAO接口
 *
 * @author xuguobing
 */
public interface UserDao extends BaseDao<User> {

    User findByUsername(String username);

    User findByOrgCodeAndJobNo(@Param("orgCode") String orgCode, @Param("jobNo") String jobNo);

    List<String> findRoleIdsByUserId(String userId);

    int deleteRoleUser(String userId);

    int insertRoleUser(RoleUser roleUser);

    int changePassword(@Param("userId") String userId, @Param("password") String newPw);

    int updateWdyId(@Param("userId") String userId, @Param("wdyId") String wdyId);

    List<User> findByOrgCode(String orgCode);
}