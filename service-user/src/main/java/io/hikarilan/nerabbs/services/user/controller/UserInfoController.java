package io.hikarilan.nerabbs.services.user.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import io.hikarilan.nerabbs.services.user.data.vo.UserBasicInfoVo;
import io.hikarilan.nerabbs.services.user.service.UserInfoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Validated
public class UserInfoController {


    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @SaCheckLogin
    @GetMapping
    @ResponseBody
    public UserBasicInfoVo getUserBasicInfo() {
        var id = StpUtil.getLoginIdAsLong();

        return userInfoService.getUserBasicInfoByID(id);
    }

    @SaCheckRole("admin")
    @GetMapping("/{id}")
    @ResponseBody
    public UserBasicInfoVo getUserBasicInfo(@PathVariable long id) {
        return userInfoService.getUserBasicInfoByID(id);
    }

}
