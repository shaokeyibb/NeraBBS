package io.hikarilan.nerabbs.services.user.database.entity;

import cn.hutool.crypto.digest.BCrypt;
import io.hikarilan.nerabbs.services.user.data.dto.UserBasicRegistrationDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Data
public final class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @Getter
    @Setter
    private long id;

    @Nullable
    @Email
    @Column(unique = true)
    @Getter
    @Setter
    private String email;

    @Nullable
    @Length(min = 8)
    @Getter
    @Setter
    private String password;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(nullable = false)
    @Getter
    @Setter
    private Date createAt;

    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @NotNull
    public static UserEntity fromUserBasicRegistrationDto(@NotNull UserBasicRegistrationDto userBasicRegistrationDto) {
        return new UserEntity(-1, userBasicRegistrationDto.email(), BCrypt.hashpw(userBasicRegistrationDto.password()), Date.from(Instant.now()), false);
    }

}
