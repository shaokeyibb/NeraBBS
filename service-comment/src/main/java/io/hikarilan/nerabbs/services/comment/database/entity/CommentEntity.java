package io.hikarilan.nerabbs.services.comment.database.entity;

import io.hikarilan.nerabbs.services.comment.data.bo.CommentCreationBo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
@Table(name = "comments", indexes = {
        @Index(columnList = "postID"),
        @Index(columnList = "rootCommentId"),
        @Index(columnList = "parentCommentId"),
        @Index(columnList = "createAt")
})
@SQLDelete(sql = "UPDATE comments SET is_deleted = true WHERE id = ?")
@SQLRestriction("is_deleted = false")
@Data
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private Long postID;

    @Column(nullable = false)
    @Getter
    @Setter
    private Long commenterID;

    @Getter
    @Setter
    @ManyToOne
    private CommentEntity rootComment;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private CommentEntity parentComment;

    @NotBlank
    @Basic(fetch = FetchType.LAZY)
    @Length(max = 65535)
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

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_frozen", nullable = false)
    @Getter
    @Setter
    private boolean frozen;

    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @NotNull
    public static CommentEntity fromCommentCreationBo(@NotNull CommentCreationBo postCreationBo) {
        return new CommentEntity(null, postCreationBo.postID(), postCreationBo.commenterID(), null, null, postCreationBo.content(), Date.from(Instant.now()), false, false);
    }

}
