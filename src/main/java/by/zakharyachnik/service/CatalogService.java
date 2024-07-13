package by.zakharyachnik.service;

import by.zakharyachnik.entity.Car;
import by.zakharyachnik.entity.Publication;
import by.zakharyachnik.entity.User;
import by.zakharyachnik.repositories.CarRepository;
import by.zakharyachnik.repositories.PublicationRepository;
import by.zakharyachnik.repositories.UserInfoRepository;
import by.zakharyachnik.repositories.UserRepository;
import by.zakharyachnik.service.exceptions.ServiceException;
import by.zakharyachnik.service.exceptions.UserNotFoundException;
import by.zakharyachnik.service.filters.CarFilterRequest;
import by.zakharyachnik.service.filters.CarSpecifications;
import by.zakharyachnik.service.filters.PublicationFilterRequest;
import by.zakharyachnik.service.filters.PublicationSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CatalogService {
    private CarRepository carRepository;
    private PublicationRepository publicationRepository;
    private UserRepository userRepository;
    private UserInfoRepository userInfoRepository;

    @Autowired
    public CatalogService(CarRepository carRepository, PublicationRepository publicationRepository, UserRepository userRepository, UserInfoRepository userInfoRepository) {
        this.carRepository = carRepository;
        this.publicationRepository = publicationRepository;
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
    }

    public List<Publication> getFilteredPublicationsList(PublicationFilterRequest publicationFilterRequest, CarFilterRequest carFilterRequest, String sortOption) {
        Specification<Publication> publicationSpecification = PublicationSpecification.withFilter(publicationFilterRequest);
        Specification<Car> carSpecification = CarSpecifications.withFilter(carFilterRequest);

        List<Publication> filteredPublications = publicationRepository.findAll(publicationSpecification);
        List<Car> filteredCars = carRepository.findAll(carSpecification);
        System.out.println(filteredCars);
        filteredPublications.removeIf(publication -> !filteredCars.contains(publication.getCar()));
        return switch (sortOption) {
            case "no-sort" -> filteredPublications;
            case "asc-sort" -> filteredPublications.stream()
                    .sorted(Comparator.comparing(Publication::getPrice))
                    .collect(Collectors.toList());
            case "desc-sort" -> filteredPublications.stream()
                    .sorted(Comparator.comparing(Publication::getPrice).reversed())
                    .collect(Collectors.toList());
            default -> throw new IllegalArgumentException("Unsupported sort option: " + sortOption);
        };
    }

    public List<Publication> getPublicationsList() {
        List<Publication> publications = publicationRepository.findAll();
        Collections.reverse(publications);
        return publications;
    }

    public List<Publication> getUserPublicationsList(String searchCriteria, String searchValue) throws UserNotFoundException {
        User user = switch (searchCriteria) {
            case "id" -> userRepository.findById(Integer.parseInt(searchValue)).get();
            case "username" -> userRepository.findByUsername(searchValue);
            case "full_name" -> userInfoRepository.findByFullName(searchValue).getUser();
            case "phone_number" -> userInfoRepository.findByPhoneNumber(searchValue).getUser();
            default -> null;
        };

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        return publicationRepository.findAllByUser(user);
    }

}
