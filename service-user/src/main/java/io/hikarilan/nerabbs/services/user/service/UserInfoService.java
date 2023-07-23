package io.hikarilan.nerabbs.services.user.service;

import io.hikarilan.nerabbs.services.user.data.vo.UserBasicInfoVo;
import io.hikarilan.nerabbs.services.user.database.repository.UserRepository;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    private final UserRepository userRepository;

    public UserInfoService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Nullable
    public UserBasicInfoVo getUserBasicInfoByID(long id) {
        return userRepository.findById(id).map(UserBasicInfoVo::fromUserEntity).orElse(null);
    }
}
