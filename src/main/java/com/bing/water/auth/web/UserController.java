/**
 *
 */
package com.bing.water.auth.web;

import com.bing.water.auth.entity.Organ;
import com.bing.water.auth.entity.Role;
import com.bing.water.auth.entity.User;
import com.bing.water.auth.service.OrganService;
import com.bing.water.auth.service.RoleService;
import com.bing.water.auth.service.UserService;
import com.bing.water.auth.vo.UserSearchVo;
import com.bing.water.common.controller.BaseController;
import com.bing.water.common.interceptor.Token;
import com.bing.water.common.model.AjaxReturn;
import com.bing.water.common.model.DtGridData;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户Controller
 *
 * @author xuguobing
 */
@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganService organService;

    @RequiresPermissions("auth:user:list")
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(Model model) {
        List<Role> roles = roleService.findRolesByUser(getSessionUser());
        model.addAttribute("roles", roles);
        List<Organ> hospitals = organService.findOrgansByPermission(getSessionUser().getId(), "auth:user:list");
        model.addAttribute("hospitals", hospitals);
        return "auth/userList";
    }

    @ResponseBody
    @RequiresPermissions("auth:user:list")
    @RequestMapping(value = "data", produces = "application/json; charset=utf-8")
    public DtGridData<User> data(UserSearchVo search) {
        if (StringUtils.isBlank(search.getOrgCode())) {
            List<String> orgCodes = Lists.newArrayList();
            List<Organ> hospitals = organService.findOrgansByPermission(getSessionUser().getId(), "auth:user:list");
            if (hospitals != null) {
                for (Organ h : hospitals) {
                    orgCodes.add(h.getCode());
                }
                search.setOrgCodes(orgCodes);
            } else {
                return new DtGridData<User>(new ArrayList<User>(), 0, search);
            }
        }
        DtGridData<User> grid = userService.findPage(search);
        return grid;
    }

    @Token(generate = true)
    @RequiresPermissions("auth:user:save")
    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(Model model, String id) {
        User user = null;
        if (StringUtils.isNotBlank(id)) {
            user = userService.findById(id);
        }
        if (user == null) {
            user = new User();
        }
        model.addAttribute("user", user);
        List<Role> roles = roleService.findRolesByUser(getSessionUser());
        model.addAttribute("roles", roles);

        List<Organ> hospitals = organService.findOrgansByPermission(getSessionUser().getId(), "auth:menu:save");
        model.addAttribute("hospitals", hospitals);
        return "auth/userForm";
    }

    @ResponseBody
    @RequiresPermissions("auth:user:del")
    @RequestMapping(value = "del", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public AjaxReturn<String> delete(String id) {
        if (StringUtils.isNotBlank(id)) {
            try {
                userService.deleteById(id);
                return new AjaxReturn<String>(true, "删除成功");
            } catch (Exception e) {
                return new AjaxReturn<String>(false, "删除异常");
            }
        } else {
            return new AjaxReturn<String>(false, "传入id不能为空");
        }
    }

    @Token(validator = true)
    @ResponseBody
    @RequiresPermissions("auth:user:save")
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public AjaxReturn<Map<String, String>> save(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return new AjaxReturn<Map<String, String>>(false, "校验失败");
        }
        try {
            return userService.saveOrUpdate(user, getSessionUser());
        } catch (Exception e) {
            logger.error("", e);
            return new AjaxReturn<Map<String, String>>(false, "保存异常");
        }
    }

    @ResponseBody
    @RequestMapping("onlyUser")
    public Object[] onlyUser(String id, String fieldId, String fieldValue) {
        if (StringUtils.isNotBlank(fieldValue)) {
            User user = userService.findByUsername(fieldValue);
            if (user != null && !user.getId().equals(id)) {
                return new Object[]{fieldId, false};
            }
        }
        return new Object[]{fieldId, true};
    }

    @ResponseBody
    @RequestMapping("onlyOrgAndJobNo")
    public Object[] onlyOrgAndJobNo(String id, String fieldId, String orgCode, String jobNo) {
        if (StringUtils.isNotBlank(orgCode) && StringUtils.isNotBlank(jobNo)) {
            User user = userService.findByOrgCodeAndJobNo(orgCode, jobNo);
            if (user != null && !user.getId().equals(id)) {
                return new Object[]{fieldId, false};
            }
        }
        return new Object[]{fieldId, true};
    }

    @ResponseBody
    @RequestMapping("changePw")
    public AjaxReturn<String> changePw(String oldPw, String newPw) {
        if (StringUtils.isBlank(oldPw) || StringUtils.isBlank(newPw)) {
            return new AjaxReturn<String>(false, "参数不能为空");
        }
        AjaxReturn<String> result = null;
        String userId = getSessionUser().getId();
        User user = userService.findById(userId);
        String enOldPw = DigestUtils.md5DigestAsHex(oldPw.getBytes());
        if (!enOldPw.equals(user.getPassword())) {
            return new AjaxReturn<String>(false, "原密码错误");
        }
        try {
            userService.changePassword(userId, DigestUtils.md5DigestAsHex(newPw.getBytes()));
            return new AjaxReturn<String>(true, "修改密码成功");
        } catch (Exception e) {
            return new AjaxReturn<String>(false, "修改密码失败");
        }
    }

    @ResponseBody
    @RequestMapping("hospitalUsers")
    public List<User> getUsers(String hospitalId) {
        List<User> users = userService.findByOrgCode(hospitalId);
        return users;
    }

}