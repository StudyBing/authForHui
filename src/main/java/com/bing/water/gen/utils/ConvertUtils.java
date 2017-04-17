package com.bing.water.gen.utils;

import com.google.common.collect.Maps;
import com.bing.water.common.validator.MemberOf;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

/**
 * Created by xuguobing on 2016/11/1 0001.
 */
public class ConvertUtils {

    private static final char SEPARATOR = '_';
    public static Map<String, Class> TYPE = Maps.newHashMap();
    public static Map<String, Class> ANN = Maps.newHashMap();
    public static Map<String, String> VDS = Maps.newHashMap();

    static {
        TYPE.put("VARCHAR", String.class);
        TYPE.put("CHAR", String.class);
        TYPE.put("TEXT", String.class);
        TYPE.put("BLOB", byte[].class);
        TYPE.put("INTEGER", Long.class);
        TYPE.put("INT", Integer.class);
        TYPE.put("TINYINT", Integer.class);
        TYPE.put("SMALLINT", Integer.class);
        TYPE.put("MEDIUMINT", Integer.class);
        TYPE.put("BIT", Boolean.class);
        TYPE.put("BIGINT", BigInteger.class);
        TYPE.put("FLOAT", Float.class);
        TYPE.put("DOUBLE", Double.class);
        TYPE.put("DECIMAL", BigDecimal.class);
        TYPE.put("BOOLEAN", Integer.class);
        TYPE.put("DATE", Date.class);
        TYPE.put("TIME", Date.class);
        TYPE.put("DATETIME", Date.class);
        TYPE.put("TIMESTAMP", Date.class);
        TYPE.put("YEAR", Date.class);


        ANN.put("Null", Null.class);
        ANN.put("NotNull", NotNull.class);
        VDS.put("NotNull", "required");
        ANN.put("AssertTrue", AssertTrue.class);
        ANN.put("AssertFalse", AssertFalse.class);
        ANN.put("Min", Min.class);
        ANN.put("Max", Max.class);
        ANN.put("DecimalMax", DecimalMax.class);
        ANN.put("DecimalMin", DecimalMin.class);
        ANN.put("Size", Size.class);
        ANN.put("Digits", Digits.class);
        ANN.put("Past", Past.class);
        ANN.put("Future", Future.class);
        ANN.put("Pattern", Pattern.class);
        ANN.put("NotBlank", NotBlank.class);
        ANN.put("Email", Email.class);
        ANN.put("Length", Length.class);
        VDS.put("Length", "maxSize");
        ANN.put("NotEmpty", NotEmpty.class);
        VDS.put("NotEmpty", "required");
        ANN.put("Range", Range.class);
        ANN.put("MemberOf", MemberOf.class);
    }

    public static String getTypeSimpleName(String key) {
        if (key != null) return null;
        Class clazz = TYPE.get(key.toUpperCase());
        if (clazz != null) {
            return clazz.getSimpleName();
        }
        return null;
    }

    public static String getTypeName(String key) {
        if (key == null) return null;
        Class clazz = TYPE.get(key.toUpperCase());
        if (clazz != null) {
            return clazz.getName();
        }
        return null;
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase("hello_world") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }

        s = s.toLowerCase();

        StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == SEPARATOR) {
                upperCase = true;
            } else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * 驼峰命名法工具
     *
     * @return toCamelCase("hello_world") == "helloWorld"
     * toCapitalizeCamelCase("hello_world") == "HelloWorld"
     * toUnderScoreCase("helloWorld") = "hello_world"
     */
    public static String toUnderScoreCase(String s) {
        if (s == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        boolean upperCase = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            boolean nextUpperCase = true;
            if (i < (s.length() - 1)) {
                nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
            }
            if ((i > 0) && Character.isUpperCase(c)) {
                if (!upperCase || !nextUpperCase) {
                    sb.append(SEPARATOR);
                }
                upperCase = true;
            } else {
                upperCase = false;
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(toCamelCase("_word"));
    }
}
