package io.hikarilan.nerabbs.services.userprofile.service;

import io.hikarilan.nerabbs.lib.services.user.grpc.BasicUserInfoRequest;
import io.hikarilan.nerabbs.lib.services.user.grpc.UserInfoGrpc;
import io.hikarilan.nerabbs.services.userprofile.data.vo.UserProfileVo;
import io.hikarilan.nerabbs.services.userprofile.database.entity.UserProfileEntity;
import io.hikarilan.nerabbs.services.userprofile.database.repository.UserProfileRepository;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.apache.seata.spring.annotation.GlobalTransactional;
import org.hibernate.validator.constraints.Length;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Validated
public class UserProfileService {

    private final AvatarService avatarService;

    private final UserProfileRepository userProfileRepository;

    @GrpcClient("nerabbs-service-user")
    private UserInfoGrpc.UserInfoBlockingStub userInfoStub;


    @Cacheable(value = "userProfile", key = "#id")
    public UserProfileVo getUserProfile(long id) {
        return UserProfileVo.fromUserProfileEntity(userProfileRepository.findById(id).orElseThrow());
    }

    @CacheEvict(value = "userProfile", key = "#id")
    @GlobalTransactional
    @Transactional
    public void updateUserProfile(long id,
                                  @NotNull @Length(min = 3) String username,
                                  @Nullable MultipartFile avatar,
                                  @NotNull @Length(max = 100) String signature,
                                  boolean checkUserExist) throws IOException {
        if (checkUserExist) {
            id = userInfoStub.getUserInfo(BasicUserInfoRequest.newBuilder().setId(id).build()).getId();
        }

        String avatarPath = null;
        if (avatar != null) {
            avatarPath = avatarService.uploadAvatar(id, avatar);
        }

        var userProfileEntity = userProfileRepository.findById(id).orElse(new UserProfileEntity());

        userProfileEntity.setUserID(id);
        userProfileEntity.setUsername(username);
        userProfileEntity.setAvatarPath(avatarPath);
        userProfileEntity.setSignature(signature);

        userProfileRepository.save(userProfileEntity);
    }

    @CacheEvict(value = "userProfile", key = "#id")
    @Transactional
    public UserProfileVo patchUserProfile(long id,
                                          @Nullable String username,
                                          @Nullable MultipartFile avatar,
                                          @Nullable String signature) throws IOException {
        var userProfileEntity = userProfileRepository.findById(id).orElseThrow();

        if (username != null) {
            userProfileEntity.setUsername(username);
        }

        if (avatar != null) {
            userProfileEntity.setAvatarPath(avatarService.uploadAvatar(id, avatar));
        }

        if (signature != null) {
            userProfileEntity.setSignature(signature);
        }

        userProfileRepository.save(userProfileEntity);
        return UserProfileVo.fromUserProfileEntity(userProfileEntity);
    }
}