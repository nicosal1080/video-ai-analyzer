package com.videoaianalyzier.video_ai_analyzer.controller;

import com.videoaianalyzier.video_ai_analyzer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthenticationController {
    private UserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationController (UserDetailsService userDetailsService,
                                     PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String loginView(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute ("user") User user, BindingResult result, Model model) {
        System.out.println("here");
        return "redirect:/";
    }
    @GetMapping("/cuenta-nueva")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/cuenta-nueva")
    public String submitRegistrationForm(Model model, @ModelAttribute ("user") User user, BindingResult result) {
        if(result.hasErrors()) {
            model.addAttribute("errorMessage",
                    "Error creando la cuenta. Inténtalo de nuevo");
            return "register :: registerFragment";
        }

        if(!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("errorMessage",
                    "Tus contraseñas no son iguales");
            return "register :: registerFragment";
        }

        UserDetails newUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles("USER")
                .build();

        // Cast to InMemoryUserDetailsManager to create the user (while I implement something with DB)
        if (userDetailsService instanceof InMemoryUserDetailsManager) {
            ((InMemoryUserDetailsManager) userDetailsService).createUser(newUser);
        }
        return "redirect:/";
    }

}
