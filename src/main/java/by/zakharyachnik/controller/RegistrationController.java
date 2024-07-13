package by.zakharyachnik.controller;

import by.zakharyachnik.service.UserService;
import by.zakharyachnik.service.exceptions.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model){
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam("full_name") String fullName,
            Model model){
        try {
            userService.registration(username, password, phoneNumber, fullName);
        } catch (ServiceException e) {
            if(e.getMessage().equals("User exists!")){
                model.addAttribute("message", "Данный логин уже занят!");
            }else{
                model.addAttribute("message", "Произошла ошибка!");
            }
            return "registration";
        }

        return "redirect:/login";
    }
}
