package io.hikarilan.nerabbs.services.webauthn.data;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.yubico.webauthn.RegisteredCredential;
import com.yubico.webauthn.data.AuthenticatorTransport;
import com.yubico.webauthn.data.UserIdentity;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.SortedSet;

@Builder
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NON_PRIVATE)
public class CredentialRegistration implements Serializable {

    @NotNull
    UserIdentity userIdentity;

    @Nullable
    String credentialNickname;

    @NotNull
    SortedSet<@NotNull AuthenticatorTransport> transports;
    @NotNull
    RegisteredCredential credential;
    @Nullable
    Object attestationMetadata;
    @NotNull
    private Instant registration;

    @JsonGetter("registration")
    public String getRegistration() {
        return registration.toString();
    }

    @JsonSetter("registration")
    public void setRegistration(String registration) {
        this.registration = Instant.parse(registration);
    }

    @JsonIgnore
    public String getUsername() {
        return userIdentity.getName();
    }

}
