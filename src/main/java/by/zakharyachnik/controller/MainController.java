package by.zakharyachnik.controller;

import by.zakharyachnik.entity.User;
import by.zakharyachnik.repositories.UserRepository;
import by.zakharyachnik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @GetMapping("/main")
    public String showUsers(
            @AuthenticationPrincipal User user,
            Model model){
        model.addAttribute("user", user);
        return "main";
    }

    @GetMapping("/")
    public String showHome(){
        return "redirect:/main";
    }

}
