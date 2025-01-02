package io.hikarilan.nerabbs.services.webauthn.database.entity;

import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import io.hikarilan.nerabbs.services.webauthn.data.CredentialRegistration;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;

import java.time.Clock;
import java.util.TreeSet;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "webauthn_credentials")
@SQLDelete(sql = "UPDATE webauthn_credentials SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Data
public class WebauthnCredentialEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private long userID;

    @Column(nullable = false, columnDefinition = "jsonb")
    @Type(JsonType.class)
    private CredentialRegistration credentialRegistration;

    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @NotNull
    public static WebauthnCredentialEntity fromFinishPasskeyRegistration(long id,
                                                                         PublicKeyCredentialCreationOptions request,
                                                                         RegistrationResult result) {
        return new WebauthnCredentialEntity(
                null,
                id,
                CredentialRegistration.builder()
                        .userIdentity(request.getUser())
                        .transports(result.getKeyId().getTransports().orElseGet(TreeSet::new))
                        .registration(Clock.systemUTC().instant())
                        .credential(RegisteredCredential.builder()
                                .credentialId(result.getKeyId().getId())
                                .userHandle(request.getUser().getId())
                                .publicKeyCose(result.getPublicKeyCose())
                                .signatureCount(result.getSignatureCount())
                                .build())
                        .build(),
                false
        );
    }

}
