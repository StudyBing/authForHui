package com.bing.water.common.interceptor;

import com.bing.water.common.utils.IdGen;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

/**
   <input type="hidden" name="UUIDTOKEN_IN_REQUEST" value="${UUIDTOKEN_IN_REQUEST}">
   <input type="hidden" name="UUIDTOKEN_FORMID_IN_REQUEST" value="${UUIDTOKEN_FORMID_IN_REQUEST}">
 * Created by xuguobing on 2016/11/29 0029.
 */
public class TokenInterceptor extends HandlerInterceptorAdapter {

    public static final String UUIDTOKEN_IN_REQUEST = "UUIDTOKEN_IN_REQUEST";
    public static final String UUIDTOKEN_FORMID_IN_REQUEST = "UUIDTOKEN_FORMID_IN_REQUEST";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Token token = method.getAnnotation(Token.class);
            if (token != null) {
                if (token.generate()) {
                    HttpSession session = request.getSession(true);
                    String uuid = IdGen.uuid();
                    String uuid_form_id = IdGen.uuid();
                    request.setAttribute(UUIDTOKEN_IN_REQUEST, uuid);
                    request.setAttribute(UUIDTOKEN_FORMID_IN_REQUEST, uuid_form_id);
                    session.setAttribute(uuid_form_id, uuid);
                }
                if (token.validator()) {
                    HttpSession session = request.getSession(false);
                    if (session != null) {
                        String uuidToken_formId = request.getParameter(UUIDTOKEN_FORMID_IN_REQUEST);
                        if (StringUtils.isNotBlank(uuidToken_formId)) {
                            String uuidToken_in_session = (String) session.getAttribute(uuidToken_formId);
                            if (StringUtils.isNotBlank(uuidToken_in_session)) {
                                String uuidToken_in_request = request.getParameter(UUIDTOKEN_IN_REQUEST);
                                if (uuidToken_in_session.equals(uuidToken_in_request)) {
                                    //清除session中的uuid防重复提交令牌
                                    session.removeAttribute(uuidToken_formId);
                                    return true;
                                }
                            }
                        }
                    }
                    return false;
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }
}
