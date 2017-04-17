package com.bing.water.auth.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by xuguobing on 2016/11/11 0011.
 */
public class FnsUtils {

    public static Boolean contains(Map<String, Object> map, String key) {
        if (map == null || StringUtils.isBlank(key)) {
            return false;
        }
        return map.containsKey(key);
    }

    public static Boolean contains(List<String> list, String o) {
        if (list == null || list.size() == 0 || o == null || "".equals(o.trim())) {
            return false;
        }
        for (String t : list) {
            if (t != null && t.trim().equals(o.trim())) {
                return true;
            }
        }
        return false;
    }

}
