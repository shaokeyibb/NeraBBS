package io.hikarilan.nerabbs.services.post.database.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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
    @NotBlank
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

}
