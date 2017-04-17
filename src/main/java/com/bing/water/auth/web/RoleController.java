/**
 *
 */
package com.bing.water.auth.web;

import com.bing.water.auth.entity.*;
import com.bing.water.auth.service.*;
import com.bing.water.auth.vo.RoleSearchVo;
import com.bing.water.common.controller.BaseController;
import com.bing.water.common.interceptor.Token;
import com.bing.water.common.model.AjaxReturn;
import com.bing.water.common.model.DtGridData;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 角色Controller
 *
 * @author xuguobing
 */
@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OrganService organService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequiresPermissions("auth:role:roleDelUser")
    @RequestMapping("/roleDelUser")
    public AjaxReturn<String> roleDelUser(String roleId, String userId) {
        try {
            return roleService.deleteRoleUser(roleId, userId);
        } catch (Exception e) {
            logger.error("移除失败:roleId={},userId={}", roleId, userId, e);
            return new AjaxReturn<String>(false, e.getMessage());
        }
    }

    @RequiresPermissions("auth:role:roleAddUser")
    @RequestMapping("/roleAddUser")
    public String roleAddUser(Model model, String roleId) {
        model.addAttribute("roleId", roleId);
        List<Organ> hospitals = organService.findOrgansByPermission(getSessionUser().getId(), "auth:user:list");
        model.addAttribute("hospitals", hospitals);
        return "auth/roleAddUser";
    }

    @ResponseBody
    @RequiresPermissions("auth:role:roleAddUser")
    @RequestMapping("/roleSaveUser")
    public AjaxReturn<String> roleSaveUser(String roleId, String userIds) {
        try {
            return userService.saveRoleUsers(roleId, userIds);
        } catch (Exception e) {
            return new AjaxReturn<String>(false, "添加用户失败");
        }
    }

    @RequiresPermissions("auth:role:roleUserList")
    @RequestMapping("/roleUserList")
    public String roleUserList(Model model, String roleId) {
        model.addAttribute("roleId", roleId);
        List<Organ> hospitals = organService.findOrgansByPermission(getSessionUser().getId(), "auth:user:list");
        model.addAttribute("hospitals", hospitals);
        return "auth/roleUserList";
    }

    @RequiresPermissions("auth:role:list")
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(Model model) {
        model.addAttribute("user", getSessionUser());
        return "auth/roleList";
    }

    @ResponseBody
    @RequiresPermissions("auth:role:list")
    @RequestMapping(value = "data", produces = "application/json; charset=utf-8")
    public DtGridData<Role> data(RoleSearchVo search) {
        User user = getSessionUser();
        if (user != null && !"1".equals(user.getId())) {
            search.setUserId(user.getId());
        }
        DtGridData<Role> grid = roleService.findPage(search);
        return grid;
    }

    @Token(generate = true)
    @RequiresPermissions("auth:role:save")
    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(Model model, String id) {
        User user = getSessionUser();
        if ("1".equals(user.getId())) { //超级管理员查询全部数据
            List<Menu> list = menuService.findMenusByTree();
            model.addAttribute("list", list);
        } else {
            List<Menu> list = menuService.findMenusByUserId(user.getId());
            model.addAttribute("list", list);
        }
        List<Organ> organs = organService.findAll();
        model.addAttribute("organs", organs);
        if (StringUtils.isNotBlank(id)) {
            Role role = roleService.findById(id);
            model.addAttribute("role", role);
        }
        return "auth/roleForm";
    }

    @ResponseBody
    @RequiresPermissions("auth:role:del")
    @RequestMapping(value = "del", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public AjaxReturn<String> delete(String id) {
        if (StringUtils.isNotBlank(id)) {
            try {
                roleService.deleteById(id);
                return new AjaxReturn<String>(true, "删除成功");
            } catch (Exception e) {
                return new AjaxReturn<String>(false, "删除异常");
            }
        } else {
            return new AjaxReturn<String>(false, "传入id不能为空");
        }
    }

    @ResponseBody
    @RequestMapping("onlyName")
    public Object[] onlyName(String id, String fieldId, String fieldValue) {
        if (StringUtils.isNotBlank(fieldValue)) {
            Role role = roleService.findByName(fieldValue);
            if (role != null && !role.getId().equals(id)) {
                return new Object[]{fieldId, false};
            }
        }
        return new Object[]{fieldId, true};
    }

    @Token(validator = true)
    @ResponseBody
    @RequiresPermissions("auth:role:save")
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public AjaxReturn<Map<String, String>> save(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            return new AjaxReturn<Map<String, String>>(false, "校验失败");
        }
        try {
            return roleService.saveOrUpdate(role, getSessionUser());
        } catch (Exception e) {
            logger.error("", e);
            return new AjaxReturn<Map<String, String>>(false, "保存异常");
        }
    }

}