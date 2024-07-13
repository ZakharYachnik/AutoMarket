package by.zakharyachnik.service;

import by.zakharyachnik.entity.Car;
import by.zakharyachnik.entity.Comment;
import by.zakharyachnik.entity.Publication;
import by.zakharyachnik.entity.User;
import by.zakharyachnik.repositories.CarRepository;
import by.zakharyachnik.repositories.CommentRepository;
import by.zakharyachnik.repositories.PublicationRepository;
import by.zakharyachnik.service.exceptions.ServiceException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class PublicationService {
    @Value("${upload.path}")
    private String uploadPath;

    private CarRepository carRepository;
    private PublicationRepository publicationRepository;
    private CommentRepository commentRepository;
    @Autowired
    public PublicationService(CarRepository carRepository, PublicationRepository publicationRepository, CommentRepository commentRepository) {
        this.carRepository = carRepository;
        this.publicationRepository = publicationRepository;
        this.commentRepository = commentRepository;
    }

    public List<Comment> getPublicationComments(Publication publication) {
        List<Comment> comments = commentRepository.findAllByPublication(publication);
        Collections.reverse(comments);
        return comments;
    }

    public void addComment(User user, Publication publication, String commentText) {
        if (commentText == null || commentText.isEmpty() || commentText.length() > 255) {
            return;
        }
        Comment comment = new Comment(commentText, user, publication);
        commentRepository.save(comment);
    }


    public void addNewPublication(
            User user, String make, String carModel,
            String year, String bodyType, String engineType,
            String driveType, String transmission, String price,
            String description, MultipartFile photo) throws ServiceException {
        try{
            int yearInt = Integer.parseInt(year);
            int priceInt = Integer.parseInt(price);

            Car car = new Car(make, carModel, yearInt, bodyType, engineType, driveType, transmission);
            saveCarPhoto(photo, car);
            carRepository.save(car);
            Publication publication = new Publication(description, LocalDate.now(), priceInt, user, car);
            publicationRepository.save(publication);
        }catch (NumberFormatException e){
            throw new ServiceException("Wrong input format!");
        } catch (IOException e) {
            throw new ServiceException("Error saving photo!");
        }
    }

    public void saveUpdatedPublication(
            User user, String make, String carModel,
            String year, String bodyType, String engineType,
            String driveType, String transmission, String price,
            String description, Publication publication) throws ServiceException {

        try {
            int yearInt = Integer.parseInt(year);
            int priceInt = Integer.parseInt(price);

            publication.getCar().setMake(make);
            publication.getCar().setModel(carModel);
            publication.getCar().setYear(yearInt);
            publication.getCar().setBodyType(bodyType);
            publication.getCar().setEngineType(engineType);
            publication.getCar().setDriveType(driveType);
            publication.getCar().setTransmission(transmission);

            publication.setDescription(description);
            publication.setPrice(priceInt);
            publicationRepository.save(publication);
        }catch (NumberFormatException e){
            throw new ServiceException("Wrong input format!");
        }
    }

    public void saveCarPhoto(MultipartFile photo, Car car) throws IOException {
        if (photo != null && !photo.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultPhotoName = uuidFile + "." + photo.getOriginalFilename();

            photo.transferTo(new File(uploadPath + "/" + resultPhotoName));

            car.setPhotoName(resultPhotoName);
        }
    }

    @Transactional
    public void deletePublication(Publication publication) {
        commentRepository.deleteByPublicationId(publication.getId());
        Car car = publication.getCar();
        publicationRepository.delete(publication);
        carRepository.delete(car);
    }
}
