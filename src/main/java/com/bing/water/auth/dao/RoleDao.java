/**
 *
 */
package com.bing.water.auth.dao;

import com.bing.water.auth.entity.*;
import com.bing.water.common.model.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色DAO接口
 *
 * @author xuguobing
 */
public interface RoleDao extends BaseDao<Role> {

    void insertRoleMenu(RoleMenu roleMenu);

    void deleteRoleMenu(String roleId);

    List<String> findMenuIdsByRoleId(String roleId);

    void deleteRoleOrgan(String roleId);

    void insertRoleOrgan(RoleOrgan roleOrgan);

    List<String> findOrganIdsByRoleId(String roleId);

    void deleteRoleSystem(String roleId);

    void insertRoleSystem(RoleSystem roleSystem);

    List<String> findSystemIdsByRoleId(String roleId);

    List<Role> findRolesByUser(User user);

    void deleteRoleUser(@Param(value = "roleId") String roleId, @Param(value = "userId") String userId);

    List<Role> findByName(@Param("name") String name);

    List<Role> findRolesByPermission(@Param("userId") String userId, @Param("permission") String permission);
}