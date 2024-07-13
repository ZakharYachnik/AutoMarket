package by.zakharyachnik.controller;

import by.zakharyachnik.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import by.zakharyachnik.entity.User;

@Controller
@RequestMapping("/profile")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping()
    public String checkProfile(
            @AuthenticationPrincipal User user,
            Model model) {

        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/edit")
    public String editProfile(
            @AuthenticationPrincipal User user,
            Model model
    ) {
        model.addAttribute("user", user);
        return "edit_profile";
    }

    @PostMapping()
    public String saveProfileChanges(
            @AuthenticationPrincipal User user,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam("full_name") String fullName
    ) {
        userService.editUserProfile(user, username, password, phoneNumber, fullName);
        return "redirect:/profile";
    }
}
