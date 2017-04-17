/**
 *
 */
package com.bing.water.auth.dao;

import com.bing.water.auth.entity.Menu;
import com.bing.water.common.model.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单DAO接口
 *
 * @author xuguobing
 */
public interface MenuDao extends BaseDao<Menu> {

    List<Menu> findMainMenus();

    List<Menu> findChildren(@Param("pid") String pid);

    void deleteChildren(Menu menu);

    List<Menu> findByUserId(String userId);

}