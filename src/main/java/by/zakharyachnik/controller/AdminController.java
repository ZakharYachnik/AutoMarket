package by.zakharyachnik.controller;

import by.zakharyachnik.entity.User;
import by.zakharyachnik.repositories.RoleRepository;
import by.zakharyachnik.repositories.UserRepository;
import by.zakharyachnik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    @Autowired
    UserService userService;
    @GetMapping()
    public String checkUsersList(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("{user}")
    public String userEditForm(
            @PathVariable User user,
            Model model) {
        model.addAttribute("user", user);
        return "edit_user_role";
    }

    @PostMapping()
    public String saveChangedUserRole(
            @RequestParam("userRole") String userRole,
            @RequestParam("userId") User user) {
        userService.changeUserRole(user, userRole);
        return "redirect:/users";
    }

    @PostMapping("/delete")
    public String deleteUserById(
            @RequestParam("userId") User user) {
        userService.deleteUserById(user.getUserId());
        return "redirect:/users";
    }
}
