package io.hikarilan.nerabbs.services.post.database.entity;

import io.hikarilan.nerabbs.services.post.data.bo.PostCreationBo;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
@SQLDelete(sql = "UPDATE posts SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted=false")
@Data
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @Getter
    @Setter
    private long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private long posterID;

    @Nullable
    @Getter
    @Setter
    private String title;

    @NotNull
    @NotBlank
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT", nullable = false)
    @Getter
    @Setter
    private String content;

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
    public static PostEntity fromPostCreationBo(@NotNull PostCreationBo postCreationBo) {
        return new PostEntity(-1, postCreationBo.userId(), postCreationBo.title(), postCreationBo.content(), Date.from(Instant.now()), false);
    }

}
