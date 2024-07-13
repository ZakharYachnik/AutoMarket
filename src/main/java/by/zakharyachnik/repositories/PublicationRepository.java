package by.zakharyachnik.repositories;

import by.zakharyachnik.entity.Publication;
import by.zakharyachnik.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Integer> {
    Publication findById(int id);

//    @Query("SELECT * FROM advertisements")
    List<Publication> findAll();

    List<Publication> findAll(Specification<Publication> publicationSpecification);

    List<Publication> findAllByUser(User user);
}
