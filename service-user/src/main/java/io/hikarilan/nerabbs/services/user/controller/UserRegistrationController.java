package io.hikarilan.nerabbs.services.user.controller;

import io.hikarilan.nerabbs.common.BizConstants;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.services.user.data.bo.UserChangePasswordBo;
import io.hikarilan.nerabbs.services.user.data.dto.UserChangePasswordDto;
import io.hikarilan.nerabbs.services.user.service.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    @PutMapping("/password")
    public void changePassword(@RequestHeader(BizConstants.USER_ID_HEADER) long userId, @RequestBody UserChangePasswordDto userChangePasswordDto) {
        if (userId == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        userRegistrationService.changePassword(UserChangePasswordBo.from(userId, userChangePasswordDto));
    }

}
