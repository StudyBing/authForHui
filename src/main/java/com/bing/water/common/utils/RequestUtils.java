package com.bing.water.common.utils;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * Created by xuguobing on 2016/12/1.
 */
public class RequestUtils {

    /**
     * 获得用户远程地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }
        return remoteAddr;
    }

    public static Set<String> getProxyAddr(HttpServletRequest request) {
        Set<String> ipSets = Sets.newHashSet();

        String remoteAddr = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(remoteAddr)) {
            ipSets.add(remoteAddr.trim());
        }

        String xxf = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(xxf)) {
            String[] arr = xxf.split(",");
            if (arr != null && arr.length > 0) {
                for (String s : arr) {
                    ipSets.add(s);
                }
            }
        }

        String host = request.getHeader("Host");
        if (StringUtils.isNotBlank(host)) {
            ipSets.add(host);
        }

        return ipSets;
    }

}
