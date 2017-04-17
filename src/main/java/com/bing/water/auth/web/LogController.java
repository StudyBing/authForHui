/**
 * 
 */
package com.bing.water.auth.web;

import com.bing.water.auth.entity.Log;
import com.bing.water.auth.entity.User;
import com.bing.water.auth.service.LogService;
import com.bing.water.auth.service.UserService;
import com.bing.water.auth.vo.LogSearchVo;
import com.bing.water.common.controller.BaseController;
import com.bing.water.common.model.DtGridData;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 日志Controller
 * @author xuguobing
 */
@Controller
@RequestMapping("log")
public class LogController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    private LogService logService;

    @Autowired
    private UserService userService;

	@RequiresPermissions("auth:log:list")
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(Model model) {
        return "auth/logList";
    }

    @ResponseBody
    @RequiresPermissions("auth:log:list")
    @RequestMapping(value = "data", produces = "application/json; charset=utf-8")
    public DtGridData<Log> data(LogSearchVo search) {
        DtGridData<Log> grid = logService.findPage(search);
        return grid;
    }

    @RequiresPermissions("auth:log:view")
    @RequestMapping(value = "form", method = RequestMethod.GET)
    public String form(Model model, String id) {
        if (StringUtils.isNotBlank(id)) {
            Log log = logService.findById(id);
            model.addAttribute("log", log);
            if (log != null){
                User user = userService.findById(log.getUserId());
                model.addAttribute("user",user);
            }
        }
        return "auth/logForm";
    }


}