package by.zakharyachnik.controller;

import by.zakharyachnik.entity.User;
import by.zakharyachnik.repositories.RoleRepository;
import by.zakharyachnik.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @GetMapping()
    public String checkUsersList(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @GetMapping("{user}")
    public String userEditForm(
            @PathVariable User user,
            Model model) {
        model.addAttribute("user", user);
        System.out.println(user.getUserId() + " " + user.getUsername());
        return "edit_user_role";
    }

    @PostMapping()
    public String saveChangedUserRole(
            @RequestParam("userRole") String userRole,
            @RequestParam("userId") User user) {
        user.setUserRole(roleRepository.findByRole(userRole));
        userRepository.save(user);
        return "redirect:/users";
    }
}
