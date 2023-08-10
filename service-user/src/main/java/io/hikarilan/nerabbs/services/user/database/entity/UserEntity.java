package io.hikarilan.nerabbs.services.user.database.entity;

import cn.hutool.crypto.digest.BCrypt;
import io.hikarilan.nerabbs.services.user.data.dto.UserBasicRegistrationDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
@Data
public final class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private long id;

    @NotNull
    @Length(min = 3)
    @NotBlank
    @Getter
    @Setter
    private String username;

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

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @NotNull
    public static UserEntity fromUserBasicRegistrationDto(@NotNull UserBasicRegistrationDto userBasicRegistrationDto) {
        return new UserEntity(-1, userBasicRegistrationDto.username(), userBasicRegistrationDto.email(), BCrypt.hashpw(userBasicRegistrationDto.password()), false);
    }

}
