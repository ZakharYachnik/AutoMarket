package by.zakharyachnik.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "comment_id", nullable = false)
    private Integer commentId;

    @Column(nullable = false, name="comment_text")
    private String commentText;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ad_id")
    private Publication publication;

    public Comment(String commentText, User user, Publication publication) {
        this.commentText = commentText;
        this.user = user;
        System.out.println(publication.getId());
        this.publication = publication;
    }
}

