/**
 *
 */
package com.bing.water.auth.web;

import com.bing.water.auth.entity.Organ;
import com.bing.water.auth.service.OrganService;
import com.bing.water.auth.vo.OrganSearchVo;
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
import java.util.Map;

/**
 * 机构Controller
 *
 * @author xuguobing
 */
@Controller
@RequestMapping("organ")
public class OrganController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(OrganController.class);

    @Autowired
    private OrganService organService;

    @RequiresPermissions("auth:organ:list")
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(Model model) {
        return "auth/organList";
    }

    @ResponseBody
    @RequiresPermissions("auth:organ:list")
    @RequestMapping(value = "data", produces = "application/json; charset=utf-8")
    public DtGridData<Organ> data(OrganSearchVo search) {
        DtGridData<Organ> grid = organService.findPage(search);
        return grid;
    }

    @Token(generate = true)
    @RequiresPermissions("auth:organ:save")
    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(Model model, String id) {
        if (StringUtils.isNotBlank(id)) {
            Organ organ = organService.findById(id);
            model.addAttribute("organ", organ);
        }
        return "auth/organForm";
    }

    @ResponseBody
    @RequiresPermissions("auth:organ:del")
    @RequestMapping(value = "del", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public AjaxReturn<String> delete(String id) {
        if (StringUtils.isNotBlank(id)) {
            try {
                organService.deleteById(id);
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
            Organ organ = organService.findByName(fieldValue);
            if (organ != null && !organ.getId().equals(id)) {
                return new Object[]{fieldId, false};
            }
        }
        return new Object[]{fieldId, true};
    }

    @ResponseBody
    @RequestMapping("onlyCode")
    public Object[] onlyCode(String id, String fieldId, String fieldValue) {
        if (StringUtils.isNotBlank(fieldValue)) {
            Organ organ = organService.findByOrgCode(fieldValue);
            if (organ != null && !organ.getId().equals(id)) {
                return new Object[]{fieldId, false};
            }
        }
        return new Object[]{fieldId, true};
    }

    @ResponseBody
    @Token(validator = true)
    @RequiresPermissions("auth:organ:save")
    @RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public AjaxReturn<Map<String, String>> save(@Valid Organ organ, BindingResult result) {
        if (result.hasErrors()) {
            return new AjaxReturn<Map<String, String>>(false, "校验失败");
        }
        try {
            return organService.saveOrUpdate(organ, getSessionUser());
        } catch (Exception e) {
            logger.error("", e);
            return new AjaxReturn<Map<String, String>>(false, "保存异常");
        }
    }

}