package by.zakharyachnik.controller;

import by.zakharyachnik.entity.User;
import by.zakharyachnik.service.PublicationService;
import by.zakharyachnik.service.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.TimeUnit;

@Controller
public class PublicationController {
    @Autowired
    private PublicationService publicationService;

    @GetMapping("/new_publication")
    public String showPublicationForm(@AuthenticationPrincipal User user,
                               Model model) {
        model.addAttribute("user", user);
        return "publication";
    }

    @PostMapping("/new_publication")
    public String saveNewPublication(
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
            @RequestParam("file") MultipartFile photo,
            Model model) {
        model.addAttribute("user", user);
        if (make.isEmpty() || carModel.isEmpty() || year.isEmpty() || bodyType.isEmpty() ||
                engineType.isEmpty() || driveType.isEmpty() || transmission.isEmpty() ||
                price.isEmpty() || description.isEmpty()) {
            model.addAttribute("message", "Заполните все поля!");
            return "publication";
        }
        try{
            publicationService.addNewPublication(user, make, carModel, year, bodyType, engineType, driveType, transmission, price, description, photo);
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


}
