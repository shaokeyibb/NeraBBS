package io.hikarilan.nerabbs.services.auth.controller;

import cn.dev33.satoken.stp.StpUtil;
import io.hikarilan.nerabbs.common.BizConstants;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.services.auth.service.PasskeyAuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/authorization/passkey")
@Validated
public class PasskeyAuthorizationController {

    private final PasskeyAuthorizationService passkeyAuthorizationService;

    @GetMapping("/registration/options")
    @ResponseBody
    public String getPasskeyRegistrationOptions(@RequestHeader(BizConstants.USER_ID_HEADER) long userID) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        return passkeyAuthorizationService.getPasskeyRegistrationOptions(userID);
    }

    @PostMapping("/registration")
    public void verifyPasskeyRegistration(@RequestHeader(BizConstants.USER_ID_HEADER) long userID, @RequestBody String credential) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        passkeyAuthorizationService.verifyPasskeyRegistration(userID, credential);
    }

    @GetMapping("/assertion/options")
    @ResponseBody
    public String getPasskeyAssertionOptions(HttpServletRequest httpServletRequest) {
        return passkeyAuthorizationService.getPasskeyAssertionOptions(httpServletRequest.getSession().getId());
    }

    @PostMapping("/assertion")
    @ResponseBody
    public void verifyPasskeyAssertion(HttpServletRequest httpServletRequest, @RequestBody String credential) {
        var id = passkeyAuthorizationService.verifyPasskeyAssertion(httpServletRequest.getSession().getId(), credential);

        StpUtil.login(id);
    }
}
