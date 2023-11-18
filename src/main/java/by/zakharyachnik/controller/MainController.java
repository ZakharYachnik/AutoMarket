package by.zakharyachnik.controller;

import by.zakharyachnik.entity.User;
import by.zakharyachnik.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/main")
    public String addUser(@ModelAttribute("user") User user){
        userRepository.save(user);
        return "redirect:/main";
    }

    @GetMapping("/main")
    public String showUsers(
            @AuthenticationPrincipal User user,
            Model model){
        model.addAttribute("user", user);
        model.addAttribute("users", userRepository.findAll());
        return "main";
    }

    @GetMapping("/")
    public String showHome(){
        return "home";
    }

}
