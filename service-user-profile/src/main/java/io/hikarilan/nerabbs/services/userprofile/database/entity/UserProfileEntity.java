package io.hikarilan.nerabbs.services.userprofile.database.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_profiles")
@SQLDelete(sql = "UPDATE user_profiles SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
@Data
@Builder
public class UserProfileEntity {

    @Id
    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private long userID;

    @Nullable
    @Length(min = 3)
    @NotBlank
    @Column
    @Getter
    @Setter
    private String username;

    @Nullable
    @Column
    @Getter
    @Setter
    private String avatarPath;

    @Nullable
    @Length(max = 100)
    @NotBlank
    @Column
    @Getter
    @Setter
    private String signature;

    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

}
