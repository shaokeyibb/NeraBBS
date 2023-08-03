package io.hikarilan.nerabbs.services.auth.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import io.hikarilan.nerabbs.services.auth.data.dto.UserBasicSignInDto;
import io.hikarilan.nerabbs.services.auth.data.dto.UserBasicSignUpDto;
import io.hikarilan.nerabbs.services.auth.service.AuthorizationValidationService;
import io.hikarilan.nerabbs.services.auth.service.BasicRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authorization")
@Validated
public class BasicAuthorizationController {

    private final AuthorizationValidationService authorizationValidationService;

    private final BasicRegistrationService basicRegistrationService;

    @SaCheckLogin
    @PostMapping("/signout")
    public void signOut() {
        StpUtil.logout();
    }

    @PostMapping("/signin")
    public void signIn(@RequestBody @Valid UserBasicSignInDto userBasicSignInDto) {
        var id = authorizationValidationService.validateUser(userBasicSignInDto);

        StpUtil.login(id);
    }

    @PostMapping("/signup")
    public void signUp(@RequestBody @Valid UserBasicSignUpDto userBasicSignUpDto) {
        var id = basicRegistrationService.registerUser(userBasicSignUpDto);

        StpUtil.login(id);

    }

}
