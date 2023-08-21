package io.hikarilan.nerabbs.services.webauthn.database.repository;

import io.hikarilan.nerabbs.services.webauthn.database.entity.WebauthnCredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WebauthnCredentialRepository extends JpaRepository<WebauthnCredentialEntity, Long> {

    List<WebauthnCredentialEntity> findAllByUserID(long userID);

//    List<WebauthnCredentialEntity> findAllByCredentialRegistrationUserIdentityId(ByteArray id);
//
//    List<WebauthnCredentialEntity> findAllByCredentialRegistrationCredentialCredentialId(ByteArray id);

}
