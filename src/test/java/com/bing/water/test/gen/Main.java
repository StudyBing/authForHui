package com.bing.water.test.gen;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.bing.water.common.model.BaseEntity;
import com.bing.water.common.utils.SpringContextHolder;
import com.bing.water.gen.model.DbColumn;
import com.bing.water.gen.model.GenColumn;
import com.bing.water.gen.model.GenTable;
import com.bing.water.gen.service.GenService;
import com.bing.water.gen.utils.ConvertUtils;
import com.bing.water.gen.utils.GenUtils;
import com.bing.water.test.BaseTest;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by xuguobing on 2016/11/1 0001.
 */

public class Main extends BaseTest {
    public static final String ENTITY_TREE = "tree";
    public static final String ENTITY_GRID = "grid";

    @Test
    public void createOrgan() {
        String schema = "healthcloud_auth_cdc";
        String tableName = "sys_organ";
        String moduleName = "auth";
        String className = "Organ";
        Boolean webFlag = true;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_GRID, "机构", "xuguobing", serviceFlag, webFlag);
    }

    @Test
    public void createSystem() {
        String schema = "healthcloud_auth_cdc";
        String tableName = "sys_system";
        String moduleName = "auth";
        String className = "System";
        Boolean webFlag = true;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_GRID, "接入系统", "xuguobing", serviceFlag, webFlag);
    }

    @Test
    public void createUser() {
        String schema = "healthcloud_auth_cdc";
        String tableName = "sys_user";
        String moduleName = "auth";
        String className = "User";
        Boolean webFlag = true;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_GRID, "用户", "xuguobing", serviceFlag, webFlag);
    }

    @Test
    public void createMenu() {
        String schema = "healthcloud_auth_cdc";
        String tableName = "sys_menu";
        String moduleName = "auth";
        String className = "Menu";
        Boolean webFlag = true;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_TREE, "菜单", "xuguobing", serviceFlag, webFlag);
    }

    @Test
    public void createRole() {
        String schema = "healthcloud_auth_cdc";
        String tableName = "sys_role";
        String moduleName = "auth";
        String className = "Role";
        Boolean webFlag = true;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_GRID, "角色", "xuguobing", serviceFlag, webFlag);
    }

    @Test
    public void createDepart() {
        String schema = "healthcloud_doc";
        String tableName = "t_dic_depart_gb";
        String moduleName = "auth";
        String className = "DicDepart";
        Boolean webFlag = false;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_TREE, "科室", "xuguobing", serviceFlag, webFlag);
    }

    @Test
    public void createDuty(){
        String schema = "healthcloud_doc";
        String tableName = "t_dic_duty";
        String moduleName = "auth";
        String className = "DicDuty";
        Boolean webFlag = false;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_TREE, "职务", "xuguobing", serviceFlag, webFlag);
    }

    @Test
    public void createLog(){
        String schema = "healthcloud_auth_cdc";
        String tableName = "sys_log";
        String moduleName = "auth";
        String className = "Log";
        Boolean webFlag = true;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_GRID, "日志", "xuguobing", serviceFlag, webFlag);
    }

    @Test
    public void createDict(){
        String schema = "healthcloud_auth_cdc";
        String tableName = "sys_dict";
        String moduleName = "auth";
        String className = "Dict";
        Boolean webFlag = true;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_GRID, "字典", "xuguobing", serviceFlag, webFlag);
    }

    @Test
    public void createMacInfo(){
        String schema = "healthcloud_auth_cdc";
        String tableName = "sys_mac_info";
        String moduleName = "auth";
        String className = "MacInfo";
        Boolean webFlag = true;
        Boolean serviceFlag = true;
        GenUtils.createMain(schema, tableName, moduleName, className, ENTITY_GRID, "Mac信息", "xuguobing", serviceFlag, webFlag);
    }
}
