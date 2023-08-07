package io.hikarilan.nerabbs.services.user.controller;

import io.hikarilan.nerabbs.common.BizConstants;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.services.user.data.vo.UserBasicInfoVo;
import io.hikarilan.nerabbs.services.user.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
public class UserInfoController {

    private final UserInfoService userInfoService;

    @GetMapping
    @ResponseBody
    public UserBasicInfoVo getUserBasicInfoFromHeader(@RequestHeader(BizConstants.USER_ID_HEADER) long userID) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        return userInfoService.getUserBasicInfoByID(userID);
    }

    //TODO: @SaCheckRole("admin")
    @GetMapping("/{id}")
    @ResponseBody
    public UserBasicInfoVo getUserBasicInfo(@PathVariable long id) {
        return userInfoService.getUserBasicInfoByID(id);
    }

}
