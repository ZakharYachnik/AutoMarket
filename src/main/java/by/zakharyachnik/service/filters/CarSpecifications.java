package by.zakharyachnik.service.filters;

import by.zakharyachnik.entity.Car;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.criteria.Predicate;

public class CarSpecifications {

    public static Specification<Car> withFilter(CarFilterRequest filterRequest) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterRequest.getMake() != null && !filterRequest.getMake().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("make"), filterRequest.getMake()));
            }

            if (filterRequest.getYearFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("year"), filterRequest.getYearFrom()));
            }

            if (filterRequest.getYearTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("year"), filterRequest.getYearTo()));
            }

            if (filterRequest.getModel() != null && !filterRequest.getModel().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("model"), "%" + filterRequest.getModel() + "%"));
            }

            if (filterRequest.getEngineType() != null && !filterRequest.getEngineType().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("engineType"), filterRequest.getEngineType()));
            }

            if (filterRequest.getDriveType() != null && !filterRequest.getDriveType().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("driveType"), filterRequest.getDriveType()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
