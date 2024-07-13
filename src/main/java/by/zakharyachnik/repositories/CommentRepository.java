package by.zakharyachnik.repositories;

import by.zakharyachnik.entity.Comment;
import by.zakharyachnik.entity.Publication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findAllByPublication(Publication publication);
    void deleteByPublicationId(Integer id);
}
