package io.hikarilan.nerabbs.services.user.service;

import io.hikarilan.nerabbs.services.user.data.vo.UserBasicInfoVo;
import io.hikarilan.nerabbs.services.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserInfoService {

    private final UserRepository userRepository;

    @Cacheable(value = "userBasicInfo", key = "#id")
    public UserBasicInfoVo getUserBasicInfoByID(long id) {
        return userRepository.findById(id).map(UserBasicInfoVo::fromUserEntity).orElseThrow();
    }
}
