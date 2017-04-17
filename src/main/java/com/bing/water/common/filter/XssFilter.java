package com.bing.water.common.filter;

import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Apple on 15/10/24.
 */
public class XssFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReqeuest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String url = httpReqeuest.getRequestURI();
        Pattern pattern = Pattern.compile("<(.*?)>");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            httpReqeuest.getRequestDispatcher(StringEscapeUtils.escapeHtml4(url)).forward(httpReqeuest, httpResponse);
        } else {
            filterChain.doFilter(new XssHttpServletRequestWrapper(httpReqeuest), servletResponse);
        }

    }

    @Override
    public void destroy() {

    }

}
