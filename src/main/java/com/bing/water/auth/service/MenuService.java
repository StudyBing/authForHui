/**
 *
 */
package com.bing.water.auth.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.bing.water.auth.dao.MenuDao;
import com.bing.water.auth.entity.Menu;
import com.bing.water.auth.entity.User;
import com.bing.water.common.model.AjaxReturn;
import com.bing.water.common.model.BaseEntity;
import com.bing.water.common.utils.IdGen;
import com.bing.water.common.utils.PinYinUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * 菜单Service
 *
 * @author xuguobing
 */
@Service
@Transactional(readOnly = true)
public class MenuService {

    private static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";
    @Autowired
    private MenuDao menuDao;

    private static String[] IGNORES;

    private Map<String, String> menuMap = null;

    static {
        List<String> list = Lists.newArrayList(BaseEntity.IGNORES);
        list.add("id");
        list.add("children");
        IGNORES = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            IGNORES[i] = list.get(i);
        }
    }

    public List<Menu> findMenusByTree() {
        List<Menu> list = menuDao.findAll();
        List<Menu> tree = convertTree(list);
        List<Menu> rs = Lists.newArrayList();
        for (Menu m : tree) {
            convertList(rs, m);
        }
        return rs;
    }

    private List<Menu> convertTree(List<Menu> list) {
        Map<String, Menu> map = Maps.newHashMap();
        for (Menu m : list) {
            map.put(m.getId(), m);
        }
        List<Menu> tree = Lists.newArrayList();
        for (Menu m : list) {
            if (StringUtils.isBlank(m.getPid()) || "0".equals(m.getPid())) {
                tree.add(m);
            } else {
                Menu pMenu = map.get(m.getPid());
                if (pMenu == null) {
                    tree.add(m);
                } else {
                    List<Menu> children = pMenu.getChildren();
                    if (children == null) {
                        children = Lists.newArrayList();
                        pMenu.setChildren(children);
                    }
                    children.add(m);
                    Collections.sort(children, new Comparator<Menu>() {
                        @Override
                        public int compare(Menu o1, Menu o2) {
                            int s1 = o1.getSort();
                            int s2 = o2.getSort();
                            return s1 - s2;
                        }
                    });
                }
            }
        }
        Collections.sort(tree, new Comparator<Menu>() {
            @Override
            public int compare(Menu o1, Menu o2) {
                int s1 = o1.getSort();
                int s2 = o2.getSort();
                return s1 - s2;
            }
        });
        return tree;
    }

    private void convertList(List<Menu> list, Menu m) {
        list.add(m);
        if (m.getChildren() != null && m.getChildren().size() > 0) {
            for (Menu tmp : m.getChildren()) {
                convertList(list, tmp);
            }
        }
    }


    public Menu findById(String id) {
        return menuDao.get(id);
    }

    @Transactional(readOnly = false)
    public AjaxReturn<Map<String, String>> saveOrUpdate(Menu vo, User user) {
        if (vo != null && StringUtils.isNotBlank(vo.getId())) { //修改
            Menu po = menuDao.get(vo.getId());
            if (po != null) {
                BeanUtils.copyProperties(vo, po, IGNORES);
                po.initByUpdate(user);
                menuDao.update(po);
                return new AjaxReturn<Map<String, String>>(true, "修改成功");
            } else {
                return new AjaxReturn<Map<String, String>>(false, "传入ID无法找到记录");
            }
        } else { //新增
            vo.setId(IdGen.uuid());
            if (StringUtils.isBlank(vo.getPid())) {
                vo.setPid("0");
            }
            vo.init(user);
            menuDao.insert(vo);
            return new AjaxReturn<Map<String, String>>(true, "保存成功");
        }
    }

    @Transactional(readOnly = false)
    public void deleteById(String id) {
//        menuDao.delete(id);
        Menu menu = menuDao.get(id);
        if (menu != null) {
            List<Menu> children = findAllChildren(id);
            menu.setChildren(children);
            menuDao.deleteChildren(menu);
        }
    }

    public List<Menu> findAllChildren(String id) {
        List<Menu> children = menuDao.findChildren(id);
        if (children != null && children.size() > 0) {
            List<Menu> cls = Lists.newArrayList();
            for (Menu m : children) {
                List<Menu> tmps = findAllChildren(m.getId());
                if (tmps.size() > 0) {
                    cls.addAll(tmps);
                }
            }
            children.addAll(cls);
        }
        return children;
    }

    private String parseTitle(Menu menu) {
        String rs = "";
        if (menu != null) {
            if (StringUtils.isNotBlank(menu.getPid())) {
                Menu pMenu = menuDao.get(menu.getPid());
                rs = parseTitle(pMenu) + rs;
            }
            if ("".equals(rs)) {
                rs = menu.getName();
            } else {
                rs = rs + "-" + menu.getName();
            }
        }
        return rs;
    }

    public String findTitleByUri(String href, String permission) {
        if (menuMap == null) {
            menuMap = Maps.newHashMap();
            List<Menu> menuList = menuDao.findAll();
            for (Menu menu : menuList) {
                // 获取菜单名称路径（如：系统设置-机构用户-用户管理-编辑）
                String namePath = parseTitle(menu);
                // 设置菜单名称路径
                if (StringUtils.isNotBlank(menu.getHref())) {
                    menuMap.put(menu.getHref(), namePath);
                }
                if (StringUtils.isNotBlank(menu.getPermission())) {
                    for (String p : StringUtils.split(menu.getPermission())) {
                        menuMap.put(p, namePath);
                    }
                }
            }
        }
        if (StringUtils.isNotBlank(href)) {
            href = StringUtils.substringAfter(href, "/health");
        }
        String menuNamePath = menuMap.get(href);
        if (menuNamePath == null) {
            for (String p : StringUtils.split(permission)) {
                menuNamePath = menuMap.get(p);
                if (StringUtils.isNotBlank(menuNamePath)) {
                    break;
                }
            }
            if (menuNamePath == null) {
                return "";
            }
        }
        return menuNamePath;
    }

    public List<Menu> findMainMenus() {
        return menuDao.findMainMenus();
    }

    public List<Menu> findMenusByUserId(String id) {
        List<Menu> list = menuDao.findByUserId(id);
        List<Menu> tree = convertTree(list);
        List<Menu> rs = Lists.newArrayList();
        for (Menu m : tree) {
            convertList(rs, m);
        }
        return rs;
    }

    public List<Menu> findMenus(String userId) {
        if (userId == null || "".equals(userId.trim())) {
            return null;
        }
        List<Menu> menus = null;
        if ("1".equals(userId)) {
            menus = menuDao.findAll();
        } else {
            menus = menuDao.findByUserId(userId);
        }
        return menus;
    }

    public List<Menu> findMenusForTree(String userId) {
        List<Menu> menus = findMenus(userId);
        return convertTree(menus);
    }
}