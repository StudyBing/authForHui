package com.bing.water.gen.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.bing.water.common.model.BaseEntity;
import com.bing.water.common.utils.FileUtils;
import com.bing.water.common.utils.SpringContextHolder;
import com.bing.water.gen.model.DbColumn;
import com.bing.water.gen.model.GenColumn;
import com.bing.water.gen.model.GenTable;
import com.bing.water.gen.model.GenTemplate;
import com.bing.water.gen.service.GenService;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuguobing on 2016/11/3 0003.
 */
public class GenUtils {
    public static Set<String> ignores = Sets.newHashSet(BaseEntity.IGNORES);

    public static void createMain(String schema, String tableName, String moduleName, String className, String entityType, String functionName, String functionAuthor, Boolean serviceFlag, Boolean webFlag) {
        if (StringUtils.isBlank(schema) || StringUtils.isBlank(tableName) || StringUtils.isBlank(moduleName) || StringUtils.isBlank(className) || StringUtils.isBlank(entityType) || StringUtils.isBlank(functionName) || StringUtils.isBlank(functionAuthor)) {
            throw new RuntimeException("参数不能为空");
        }
        Map<String, Object> configs = Maps.newHashMap();
        configs.put("ClassName", className); //java实体类名
        configs.put("entityType", entityType); //tree为树机构，grid为表格格式
        configs.put("functionName", functionName); //表功能描述
        configs.put("functionAuthor", functionAuthor); //作者
        String packageName = "com.bing.water";
        configs.put("packageName", packageName);
        configs.put("moduleName", moduleName);
        configs.put("schema", schema);
        configs.put("tableName", tableName);
        configs.put("serviceFlag", serviceFlag ? "1" : "0");
        configs.put("webFlag", webFlag ? "1" : "0");

        GenService genService = SpringContextHolder.getBean(GenService.class);
        List<DbColumn> cols = genService.findColumns(schema, tableName);

        if (cols == null || cols.size() == 0) {
            throw new RuntimeException("表未找到记录");
        }

        ignores.add("id");

        GenTable table = new GenTable();
        table.setSchema(schema);
        table.setTable(tableName);

        for (DbColumn c : cols) {
            addColumn(table, c);
        }
        configs.put("table", table);

        GenUtils.genMainFile(configs);

    }

    private static void addColumn(GenTable table, DbColumn c) {
        GenColumn col = new GenColumn();
        // 列名
        col.setName(c.getName());
        // 描述
        col.setComments(c.getComment());
        // JDBC类型
        col.setJdbcType(c.getType());
        // JAVA类型
        col.setJavaType(ConvertUtils.getTypeName(c.getType()));
        // JAVA字段名
        col.setJavaField(ConvertUtils.toCamelCase(c.getName()));
        col.setPk("PRI".equals(c.getColKey()) ? "1" : "0"); // 是否主键
        if (ignores.contains(col.getJavaField())) {
            col.setEdit("0");// 是否编辑字段
            col.setList("0");// 是否列表字段
        } else {
            col.setEdit("1");
            col.setList("1");
        }

        col.setShowType("GenColumn.SHOW_TYPE_INPUT");// 字段生成方案
        col.setQuery("0");// 是否查询字段
        if ("java.util.Date".equals(col.getJavaType())) {
            col.setQueryType("GenColumn.QUERY_TYPE_BW");// 查询方式
        } else {
            col.setQueryType("GenColumn.QUERY_TYPE_EQ");// 查询方式
        }
        col.setDictType("");// 字典类型

        List<String> vs = Lists.newArrayList();
        if ("java.lang.String".equals(col.getJavaType()) && c.getLength() != null && c.getLength().length() > 0) {
            vs.add("@Length(max=" + c.getLength() + ")");
        }
        if ("NO".equals(c.getIsNull()) && !"id".equals(c.getName())) {
            if ("".equals(col.getJavaType())) {
                vs.add("@NotEmpty");
            } else {
                vs.add("@NotNull");
            }
        }
        col.setLength(c.getLength());
        col.setValidators(StringUtils.join(vs.toArray(), "#"));
        table.addColumn(col);
    }

    public static void parseTableColumn(GenTable table, String[][] columns) {
        for (String[] arr : columns) {
            GenColumn col = new GenColumn();
            col.setName(arr[0]);
            if (StringUtils.isNotBlank(arr[1])) {
                col.setComments(arr[1]);
            }
            col.setJdbcType(arr[2]);
            try {
                Class clazz = Class.forName(arr[3]);
                col.setJavaType(clazz.getSimpleName());//
                if ("1".equals(arr[8])) {
                    table.addSearchIm(arr[3]);
                }
            } catch (ClassNotFoundException e) {
            }
            if (!GenUtils.ignores.contains(arr[4])) {
                table.addEntityIm(arr[3]);
            }
            col.setJavaField(arr[4]);
            col.setPk(arr[5]); // 是否主键
            col.setEdit(arr[6]);
            col.setList(arr[7]);
            col.setShowType(arr[10]);// 字段生成方案
            col.setQuery(arr[8]);// 是否查询字段
            col.setQueryType(arr[9]);// 查询方式
            col.setDictType(arr[11]);// 字典类型
            col.setValidators(arr[12].trim());//校验器列表
            Integer length = null;
            if (StringUtils.isNotBlank(arr[13])) {
                length = Integer.parseInt(arr[13].trim());
            }
            String[] as = arr[12].split("#");
            StringBuffer vds = new StringBuffer(20);
            for (String s : as) {
                if (StringUtils.isNotBlank(s)) {
                    s = s.trim();
                    if (s.length() > 1) {
                        int end = s.indexOf("(");
                        if (end > 0) {
                            s = s.substring(1, end);
                        } else {
                            s = s.substring(1);
                        }
                        Class clazz = ConvertUtils.ANN.get(s);
                        if (clazz != null) {
                            table.addEntityIm(clazz.getName());
                        }
                        String vd = ConvertUtils.VDS.get(s);
                        if (StringUtils.isNotBlank(vd)) {
                            if (vds.length() > 0) {
                                vds.append(",");
                            }
                            if ("MaxSize".equals(vd) && length != null) {
                                vds.append("MaxSize[" + length + "]");
                            } else if ("java.lang.Integer".equals(arr[3])) {
                                vds.append("custom[integer]");
                            } else {
                                vds.append(vd);
                            }
                        }
                    }
                }
            }
            col.setValidatorArr(as);
            if (vds.length() > 0) {
                col.setJsValidator("validate[" + vds + "]");
            }

            table.addColumn(col);
        }
    }

    public static void genJspFile(Map<String, Object> configs, String name) {
        String packageName = configs.get("packageName").toString();
        String moduleName = configs.get("moduleName").toString();
        String baseUrl = FreeMarkers.getBaseJavaUrl(packageName, "gen");
        String entityXml = baseUrl + "template" + File.separator + name + ".xml";
        GenTemplate tpl = FreeMarkers.fileToObject(entityXml, GenTemplate.class);

        String fileName = FreeMarkers.renderString(tpl.getFileName(), configs);
        String content = FreeMarkers.renderString(StringUtils.trimToEmpty(tpl.getContent()), configs);

        String moduleUrl = FreeMarkers.getBaseJspUrl(moduleName);
        String newFile = moduleUrl + fileName;
        //删除文件
        FileUtils.deleteFile(newFile);
        // 创建并写入文件
        if (FileUtils.createFile(newFile)) {
            FileUtils.writeToFile(newFile, content, true);
        }
    }

    /**
     * @param configs
     */
    public static void genMybatisXml(Map<String, Object> configs) {
        String packageName = configs.get("packageName").toString();
        String moduleName = configs.get("moduleName").toString();
        String baseUrl = FreeMarkers.getBaseJavaUrl(packageName, "gen");
        String entityXml = baseUrl + "template" + File.separator + "mapper.xml";
        GenTemplate tpl = FreeMarkers.fileToObject(entityXml, GenTemplate.class);

        String fileName = FreeMarkers.renderString(tpl.getFileName(), configs);
        String content = FreeMarkers.renderString(StringUtils.trimToEmpty(tpl.getContent()), configs);

        String moduleUrl = FreeMarkers.getResourceUrl(moduleName);
        String newFile = moduleUrl + fileName;
        //删除文件
        FileUtils.deleteFile(newFile);
        // 创建并写入文件
        if (FileUtils.createFile(newFile)) {
            FileUtils.writeToFile(newFile, content, true);
        }
    }

    /**
     * @param configs
     */
    public static void genJavaFile(Map<String, Object> configs, String name, String path) {
        String packageName = configs.get("packageName").toString();
        String moduleName = configs.get("moduleName").toString();
        String baseUrl = FreeMarkers.getBaseJavaUrl(packageName, "gen");
        String entityXml = baseUrl + "template" + File.separator + name + ".xml";
        GenTemplate tpl = FreeMarkers.fileToObject(entityXml, GenTemplate.class);

        String fileName = FreeMarkers.renderString(tpl.getFileName(), configs);
        String content = FreeMarkers.renderString(StringUtils.trimToEmpty(tpl.getContent()), configs);

        String moduleUrl = FreeMarkers.getBaseJavaUrl(packageName, moduleName);
        String newFile = moduleUrl + path + File.separator + fileName;
        //删除文件
        FileUtils.deleteFile(newFile);
        // 创建并写入文件
        if (FileUtils.createFile(newFile)) {
            FileUtils.writeToFile(newFile, content, true);
        }
    }

    /**
     * 生成模板
     *
     * @param configs
     */
    public static void genMainFile(Map<String, Object> configs) {
        String packageName = configs.get("packageName").toString();
        String moduleName = configs.get("moduleName").toString();
        String baseUrl = FreeMarkers.getBaseJavaUrl(packageName, "gen");
        String mainXml = baseUrl + "template" + File.separator + "main.xml";
        GenTemplate tpl = FreeMarkers.fileToObject(mainXml, GenTemplate.class);

        String fileName = FreeMarkers.renderString(tpl.getFileName(), configs);
        String content = FreeMarkers.renderString(StringUtils.trimToEmpty(tpl.getContent()), configs);

        String testBaseUrl = FreeMarkers.getTestBaseUrl(packageName, moduleName);
        String newFile = testBaseUrl + fileName;
        //删除文件
        FileUtils.deleteFile(newFile);

        // 创建并写入文件
        if (FileUtils.createFile(newFile)) {
            FileUtils.writeToFile(newFile, content, true);
        }
    }
}
