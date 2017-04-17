/**
 *
 */
package com.bing.water.common.utils;

import com.bing.water.auth.service.LogService;
import com.bing.water.auth.service.MenuService;
import com.bing.water.common.controller.BaseController;
import com.bing.water.auth.entity.Log;
import com.bing.water.auth.entity.User;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字典工具类
 *
 * @version 2014-11-7
 */
public class LogUtils {

    public static final String CACHE_MENU_NAME_PATH_MAP = "menuNamePathMap";

//    private static LogService logService = SpringContextHolder.getBean(LogService.class);
//    private static MenuService menuService = SpringContextHolder.getBean(MenuService.class);

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, String title) {
        saveLog(request, null, null, title, null);
    }

    /**
     * 保存日志
     */
    public static void saveLog(HttpServletRequest request, Object handler, Exception ex, String title, Long startTime) {
        User user = BaseController.getSessionUser();
        if (user != null && user.getId() != null) {
            Log log = new Log();
            log.setTitle(title);
            if (startTime != null) {
                log.setRunTime(System.currentTimeMillis() - startTime);
            }
            log.setUserId(user.getId());
            log.setCreateDate(new Date());
            log.setType(ex == null ? "1" : "2");
            log.setRemoteIp(RequestUtils.getRemoteAddr(request));
            log.setUserAgent(request.getHeader("user-agent"));
            log.setRequestUri(request.getRequestURI());
            log.setParams(parseParams(request.getParameterMap(), ex != null));
            log.setMethod(request.getMethod());
            // 异步保存日志
            new SaveLogThread(log, handler, ex).start();
        }
    }

    /**
     * 设置请求参数
     *
     * @param paramMap
     */
    public static String parseParams(Map paramMap, Boolean hasEx) {
        if (paramMap == null) {
            return null;
        }
        StringBuilder params = new StringBuilder();
        for (Map.Entry<String, String[]> param : ((Map<String, String[]>) paramMap).entrySet()) {
            params.append(("".equals(params.toString()) ? "" : "&") + param.getKey() + "=");
            String paramValue = (param.getValue() != null && param.getValue().length > 0 ? param.getValue()[0] : "");
            if (hasEx) {
                params.append(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue);
            } else {
                params.append(abbr(StringUtils.endsWithIgnoreCase(param.getKey(), "password") ? "" : paramValue, 100));
            }
        }
        return params.toString();
    }

    /**
     * 缩略字符串（不区分中英文字符）
     *
     * @param str    目标字符串
     * @param length 截取长度
     * @return
     */
    public static String abbr(String str, int length) {
        if (str == null) {
            return "";
        }
        try {
            StringBuilder sb = new StringBuilder();
            int currentLength = 0;
            for (char c : replaceHtml(StringEscapeUtils.unescapeHtml4(str)).toCharArray()) {
                currentLength += String.valueOf(c).getBytes("GBK").length;
                if (currentLength <= length - 3) {
                    sb.append(c);
                } else {
                    sb.append("...");
                    break;
                }
            }
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 替换掉HTML标签方法
     */
    public static String replaceHtml(String html) {
        if (StringUtils.isBlank(html)) {
            return "";
        }
        String regEx = "<.+?>";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(html);
        String s = m.replaceAll("");
        return s;
    }

    /**
     * 保存日志线程
     */
    public static class SaveLogThread extends Thread {

        private Log log;
        private Object handler;
        private Exception ex;
        private MenuService menuService = SpringContextHolder.getBean(MenuService.class);
        private LogService logService = SpringContextHolder.getBean(LogService.class);

        public SaveLogThread(Log log, Object handler, Exception ex) {
            super(SaveLogThread.class.getSimpleName());
            this.log = log;
            this.handler = handler;
            this.ex = ex;
        }

        @Override
        public void run() {
            // 获取日志标题
            if (StringUtils.isBlank(log.getTitle())) {
                String permission = "";
                if (handler != null && handler instanceof HandlerMethod) {
                    Method m = ((HandlerMethod) handler).getMethod();
                    RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
                    permission = (rp != null ? StringUtils.join(rp.value(), ",") : "");
                }

                log.setTitle(menuService.findTitleByUri(log.getRequestUri(), permission));
            }
            // 如果有异常，设置异常信息
            log.setException(Exceptions.getStackTraceAsString(ex));
            // 如果无标题并无异常日志，则不保存信息
            if (StringUtils.isBlank(log.getTitle()) && StringUtils.isBlank(log.getException())) {
                return;
            }
            logService.insert(log);
        }
    }


}
