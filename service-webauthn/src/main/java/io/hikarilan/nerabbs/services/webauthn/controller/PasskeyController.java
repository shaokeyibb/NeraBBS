package io.hikarilan.nerabbs.services.webauthn.controller;

import io.hikarilan.nerabbs.common.BizConstants;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.services.webauthn.data.vo.PasskeyVo;
import io.hikarilan.nerabbs.services.webauthn.service.PasskeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/webauthn/passkey")
@Validated
public class PasskeyController {

    private final PasskeyService passkeyService;

    @GetMapping
    public List<PasskeyVo> getPasskeys(@RequestHeader(BizConstants.USER_ID_HEADER) long userID) {
        return passkeyService.getPasskeys(userID);
    }

    @DeleteMapping("/{id}")
    public void deletePasskey(@RequestHeader(BizConstants.USER_ID_HEADER) long userID, @PathVariable long id) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED) {
            throw new UnauthorizedException();
        }

        var passkey = passkeyService.getPasskey(id).orElseThrow();

        if (passkey.userId() != userID) {
            throw new NoSuchElementException();
        }

        passkeyService.deletePasskey(passkey.id());
    }
}
