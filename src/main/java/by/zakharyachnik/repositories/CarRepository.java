package by.zakharyachnik.repositories;

import by.zakharyachnik.entity.Car;
import by.zakharyachnik.entity.Publication;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    Car findById(int id);
    List<Car> findAll();
    List<Car> findAll(Specification<Car> carSpecification);
}
