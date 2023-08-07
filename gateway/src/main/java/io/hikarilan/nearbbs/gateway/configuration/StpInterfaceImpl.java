package io.hikarilan.nearbbs.gateway.configuration;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of(); // TODO: 返回此 loginId 拥有的权限列表
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        return List.of(); // TODO: 返回此 loginId 拥有的角色列表
    }

}
