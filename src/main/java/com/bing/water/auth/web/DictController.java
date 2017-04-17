/**
 *
 */
package com.bing.water.auth.web;

import com.bing.water.auth.entity.Dict;
import com.bing.water.auth.service.DictService;
import com.bing.water.auth.vo.DictSearchVo;
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
 * 字典Controller
 *
 * @author xuguobing
 */
@Controller
@RequestMapping("dict")
public class DictController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(DictController.class);

    @Autowired
    private DictService dictService;

    @RequiresPermissions("auth:dict:list")
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(Model model) {
        List<String> types = dictService.findGroupType();
        model.addAttribute("types", types);
        return "auth/dictList";
    }

    @ResponseBody
    @RequiresPermissions("auth:dict:list")
    @RequestMapping(value = "data", produces = "application/json; charset=utf-8")
    public DtGridData<Dict> data(DictSearchVo search) {
        return dictService.findPage(search);
    }

    @Token(generate = true)
    @RequiresPermissions("auth:dict:save")
    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(Model model, String id) {
        if (StringUtils.isNotBlank(id)) {
            Dict dict = dictService.findById(id);
            model.addAttribute("dict", dict);
        }
        return "auth/dictForm";
    }

    @ResponseBody
    @RequiresPermissions("auth:dict:del")
    @RequestMapping(value = "del", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public AjaxReturn<String> delete(String id) {
        if (StringUtils.isNotBlank(id)) {
            try {
                dictService.deleteById(id);
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
    @RequiresPermissions("auth:dict:save")
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public AjaxReturn<Map<String, String>> save(@Valid Dict dict, BindingResult result) {
        if (result.hasErrors()) {
            return new AjaxReturn<Map<String, String>>(false, "校验失败");
        }
        try {
            return dictService.saveOrUpdate(dict, getSessionUser());
        } catch (Exception e) {
            logger.error("", e);
            return new AjaxReturn<Map<String, String>>(false, "保存异常");
        }
    }

}