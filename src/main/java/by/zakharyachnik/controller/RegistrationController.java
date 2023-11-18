package by.zakharyachnik.controller;

import by.zakharyachnik.entity.User;
import by.zakharyachnik.entity.UserRole;
import by.zakharyachnik.repositories.RoleRepository;
import by.zakharyachnik.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") User user, Model model){
        User userFromDb = userRepository.findByUsername(user.getUsername());

        if(userFromDb != null){
            model.addAttribute("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        UserRole userRole = roleRepository.findByRole("USER");
        user.setUserRole(userRole);

        userRepository.save(user);

        return "redirect:/login";

    }
}
