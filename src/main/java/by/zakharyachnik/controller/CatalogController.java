package by.zakharyachnik.controller;

import by.zakharyachnik.entity.Car;
import by.zakharyachnik.entity.Publication;
import by.zakharyachnik.entity.User;
import by.zakharyachnik.repositories.CarRepository;
import by.zakharyachnik.repositories.UserRepository;
import by.zakharyachnik.service.CatalogService;
import by.zakharyachnik.service.PublicationService;
import by.zakharyachnik.service.UserService;
import by.zakharyachnik.service.exceptions.ServiceException;
import by.zakharyachnik.service.exceptions.UserNotFoundException;
import by.zakharyachnik.service.filters.CarFilterRequest;
import by.zakharyachnik.service.filters.CarSpecifications;
import by.zakharyachnik.service.filters.PublicationFilterRequest;
import by.zakharyachnik.service.filters.PublicationSpecification;
import jakarta.persistence.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class CatalogController {

    private CatalogService catalogService;
    private PublicationService publicationService;
    private UserService userService;

    @Autowired
    public CatalogController(CatalogService catalogService, PublicationService publicationService, UserService userService) {
        this.catalogService = catalogService;
        this.publicationService = publicationService;
        this.userService = userService;
    }


    @GetMapping("/catalog")
    public String showCatalog(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("publications", catalogService.getPublicationsList());
        model.addAttribute("user", user);
        return "catalog";
    }

    @PostMapping("/catalog/filter")
    public String filter(
            @AuthenticationPrincipal User user,
            PublicationFilterRequest publicationFilterRequest,
            CarFilterRequest carFilterRequest,
            @RequestParam("sort-option") String sortOption,
            Model model
    ){
        model.addAttribute("publications", catalogService.getFilteredPublicationsList(publicationFilterRequest, carFilterRequest, sortOption));
        model.addAttribute("user", user);
        return "catalog";
    }

    @PostMapping("/catalog/search_user_posts")
    public String searchUserPosts(
            @AuthenticationPrincipal User user,
            @RequestParam("searchCriteria") String searchCriteria,
            @RequestParam("searchValue") String searchValue,
            Model model
    ){
        model.addAttribute("user", user);
        try {
            model.addAttribute("publications", catalogService.getUserPublicationsList(searchCriteria, searchValue));
            return "catalog";
        } catch (UserNotFoundException e) {
            model.addAttribute("userNotFoundMessage", "Пользователь с такими данными не был найден");
            model.addAttribute("publications", catalogService.getPublicationsList());
            return "catalog";
        }

    }

    @GetMapping("/catalog/{publication}")
    public String showPublication(
            @AuthenticationPrincipal User user,
            @PathVariable("publication") Publication publication,
            Model model
    ){
        System.out.println(userService.getIsFavorite(user, publication));
        model.addAttribute("isFavorite", userService.getIsFavorite(user, publication));
        model.addAttribute("user", user);
        model.addAttribute("publication", publication);
        model.addAttribute("comments", publicationService.getPublicationComments(publication));
        return "publication_information";
    }

    @PostMapping("/catalog/{publication}/comment")
    public String addNewComment(
        @AuthenticationPrincipal User user,
        @PathVariable("publication") Publication publication,
        @RequestParam("commentText") String commentText
    ){
        publicationService.addComment(user, publication, commentText);
        return "redirect:/catalog/" + publication.getId();
    }

    @GetMapping("/catalog/{publication}/edit")
    public String editPublication(
            @AuthenticationPrincipal User user,
            @PathVariable("publication") Publication publication,
            Model model
    ){
        model.addAttribute("user", user);
        model.addAttribute("publication", publication);
        return "edit_publication";
    }

    @PostMapping("/catalog/{publication}/edit")
    public String saveEditPublication(
            @AuthenticationPrincipal User user,
            @RequestParam("make") String make,
            @RequestParam("model") String carModel,
            @RequestParam("year") String year,
            @RequestParam("body_type") String bodyType,
            @RequestParam("engine_type") String engineType,
            @RequestParam("drive_type") String driveType,
            @RequestParam("transmission") String transmission,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            @PathVariable("publication") Publication publication,
            Model model){
        model.addAttribute("user", user);
        if (make.isEmpty() || carModel.isEmpty() || year.isEmpty() || bodyType.isEmpty() ||
                engineType.isEmpty() || driveType.isEmpty() || transmission.isEmpty() ||
                price.isEmpty() || description.isEmpty()) {
            model.addAttribute("message", "Заполните все поля!");
            return "publication";
        }
        try{
            publicationService.saveUpdatedPublication(user, make, carModel, year, bodyType, engineType, driveType, transmission, price, description, publication);
            TimeUnit.MILLISECONDS.sleep(2000);
            return "redirect:/main";
        }catch (ServiceException e){
            model.addAttribute("message", "Данные некорректны! Попробуйте еще раз!");
            return "publication";
        } catch (InterruptedException e) {
            model.addAttribute("message", "Произошла ошибка! Попробуйте еще раз.");
            return "publication";
        }
    }

    @PostMapping("/catalog/{publication}/delete")
    public String deletePublication(
            @PathVariable("publication") Publication publication
    ){
        try {
            publicationService.deletePublication(publication);
            TimeUnit.MILLISECONDS.sleep(2000);
            return "redirect:/main";
        } catch (InterruptedException e) {
            return "redirect:/main";
        }
    }

    @PostMapping("/catalog/{publication}/add_to_favorite")
    public String addToFavorite(
            @PathVariable("publication") Publication publication,
            @AuthenticationPrincipal User user
    ){
        userService.addFavoritePublication(user, publication);
        return "redirect:/catalog/" + publication.getId();
    }

    @PostMapping("/catalog/{publication}/delete_from_favorite")
    public String deleteFromFavorite(
            @PathVariable("publication") Publication publication,
            @AuthenticationPrincipal User user
    ){
        userService.deleteFavoritePublication(user, publication);
        return "redirect:/catalog/" + publication.getId();
    }

    @PostMapping("/catalog/favorites")
    public String showFavorites(
            @AuthenticationPrincipal User user,
            Model model
    ){
        model.addAttribute("user", user);
        model.addAttribute("publications", userService.getFavoritePublications(user));
        return "catalog";
    }

    @PostMapping("/catalog/user_publications")
    public String showUserPublications(
            @AuthenticationPrincipal User user,
            Model model
    ){
        model.addAttribute("user", user);
        model.addAttribute("publications", userService.getUserPublicationsList(user));
        return "catalog";
    }
}
