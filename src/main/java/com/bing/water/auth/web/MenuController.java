/**
 *
 */
package com.bing.water.auth.web;

import com.bing.water.auth.entity.Menu;
import com.bing.water.auth.service.MenuService;
import com.bing.water.common.controller.BaseController;
import com.bing.water.common.interceptor.Token;
import com.bing.water.common.model.AjaxReturn;
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
 * 菜单Controller
 *
 * @author xuguobing
 */
@Controller
@RequestMapping("menu")
public class MenuController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @RequiresPermissions("auth:menu:list")
    @RequestMapping(value = {"list"}, method = RequestMethod.GET)
    public String menuList(Model model) {
        List<Menu> list = menuService.findMenusByTree();
        model.addAttribute("list", list);
        return "auth/menuList";
    }

    @Token(generate = true)
    @RequiresPermissions("auth:menu:save")
    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(Model model, String id, String pid) {
        Menu menu = null;
        if (StringUtils.isNotBlank(id)) {
            menu = menuService.findById(id);
            model.addAttribute("menu", menu);
        } else if (StringUtils.isNotBlank(pid)) {
            menu = new Menu();
            menu.setPid(pid);
            model.addAttribute("menu", menu);
        }
        List<Menu> list = menuService.findMenusByTree();
        model.addAttribute("list", list);

        if (menu == null || StringUtils.isBlank(menu.getId())
                || (StringUtils.isNotBlank(menu.getPid()) && !"0".equals(menu.getPid()))) {
            model.addAttribute("showRoot", true);
        }

        return "auth/menuForm";
    }

    @ResponseBody
    @RequiresPermissions("auth:menu:del")
    @RequestMapping(value = "del", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public AjaxReturn<String> delete(String id) {
        if (StringUtils.isNotBlank(id)) {
            try {
                menuService.deleteById(id);
                return new AjaxReturn<String>(true, "删除成功");
            } catch (Exception e) {
                logger.error("", e);
                return new AjaxReturn<String>(false, "删除异常");
            }
        } else {
            return new AjaxReturn<String>(false, "传入id不能为空");
        }
    }

    @Token(validator = true)
    @ResponseBody
    @RequiresPermissions("auth:menu:save")
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public AjaxReturn<Map<String, String>> save(@Valid Menu menu, BindingResult result) {
        if (result.hasErrors()) {
            return new AjaxReturn<Map<String, String>>(false, "校验失败");
        }
        try {
            return menuService.saveOrUpdate(menu, getSessionUser());
        } catch (Exception e) {
            logger.error("", e);
            return new AjaxReturn<Map<String, String>>(false, "保存异常");
        }
    }

}