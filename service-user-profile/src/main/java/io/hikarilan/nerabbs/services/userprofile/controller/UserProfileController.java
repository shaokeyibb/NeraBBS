package io.hikarilan.nerabbs.services.userprofile.controller;

import io.hikarilan.nerabbs.common.BizConstants;
import io.hikarilan.nerabbs.common.exception.UnauthorizedException;
import io.hikarilan.nerabbs.services.userprofile.data.vo.UserProfileVo;
import io.hikarilan.nerabbs.services.userprofile.service.UserProfileService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users/profile")
@Validated
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    @ResponseBody
    public UserProfileVo getUserProfileFromHeader(@RequestHeader(BizConstants.USER_ID_HEADER) long userID) {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED)
            throw new UnauthorizedException();

        return userProfileService.getUserProfile(userID);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public UserProfileVo getUserProfile(@PathVariable("id") long userID) {
        return userProfileService.getUserProfile(userID);
    }

    @PutMapping
    public void updateUserProfile(@RequestHeader(BizConstants.USER_ID_HEADER) long userID,
                                  @RequestParam @NotNull @Length(min = 3) String username,
            /* null means reset avatar */ @RequestPart @Nullable MultipartFile avatar,
                                  @RequestParam @NotNull @Length(max = 100) String signature) throws IOException {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED) {
            throw new UnauthorizedException();
        }

        userProfileService.updateUserProfile(userID, username, avatar, signature);
    }

    @PatchMapping
    @ResponseBody
    public UserProfileVo patchUserProfile(@RequestHeader(BizConstants.USER_ID_HEADER) long userID,
            /* can be undefined */@Nullable String username,
            /* can be undefined, null means reset avatar */ @Nullable MultipartFile avatar,
            /* can be undefined */@Nullable String signature) throws IOException {
        if (userID == BizConstants.USER_ID_UNAUTHORIZED) {
            throw new UnauthorizedException();
        }

        return userProfileService.patchUserProfile(userID, username, avatar, signature);
    }

}
