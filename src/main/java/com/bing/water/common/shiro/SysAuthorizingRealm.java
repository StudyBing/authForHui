package com.bing.water.common.shiro;

import com.bing.water.auth.entity.Menu;
import com.bing.water.auth.entity.Role;
import com.bing.water.auth.entity.User;
import com.bing.water.auth.service.MenuService;
import com.bing.water.auth.service.RoleService;
import com.bing.water.auth.service.UserService;
import com.bing.water.common.utils.SpringContextHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by xuguobing on 2017/4/11.
 */
@Service
public class SysAuthorizingRealm extends AuthorizingRealm {

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;

        UserService userService = SpringContextHolder.getBean(UserService.class);
        User user = userService.findByUsername(token.getUsername());
        if (user != null) {
            return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
        } else {
            throw new AuthenticationException("msg:" + "用户账号不存在");
        }
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        User user = (User) getAvailablePrincipal(principals);
        MenuService menuService = SpringContextHolder.getBean(MenuService.class);
        List<Menu> menus = menuService.findMenus(user.getId());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (Menu menu : menus) {
            if (StringUtils.isNotBlank(menu.getPermission())) {
                info.addStringPermission(menu.getPermission());
            }
        }

        RoleService roleService = SpringContextHolder.getBean(RoleService.class);
        List<Role> roles = roleService.findRolesByUser(user);
        for (Role role : roles) {
            if (StringUtils.isNotBlank(role.getName())) {
                info.addRole(role.getName());
            }
        }
        return info;
    }


    /**
     * 设定密码校验的Hash算法与迭代次数
     */
    @PostConstruct
    public void initCredentialsMatcher() {
        CustomCredentialsMatcher matcher = new CustomCredentialsMatcher();
        setCredentialsMatcher(matcher);
    }

    public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
        @Override
        public boolean doCredentialsMatch(AuthenticationToken authcToken, AuthenticationInfo info) {
            UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
            SimpleAuthenticationInfo simpleInfo = (SimpleAuthenticationInfo) info;
            String vPw = DigestUtils.md5DigestAsHex(String.valueOf(token.getPassword()).getBytes());
            String nPw = simpleInfo.getCredentials().toString();
            return vPw.equals(nPw);
        }
    }

}
