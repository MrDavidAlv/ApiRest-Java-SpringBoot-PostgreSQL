package com.litethinking.enterprise.interfaces.rest;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/util")
public class UtilityController {

    @GetMapping("/hash/{password}")
    public String hashPassword(@PathVariable String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        String hash = encoder.encode(password);
        boolean matches = encoder.matches(password, hash);
        return String.format("Password: %s%nHash: %s%nMatches: %s", password, hash, matches);
    }

    @PostMapping("/verify")
    public String verifyPassword(@RequestParam String password, @RequestParam String hash) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        boolean matches = encoder.matches(password, hash);
        return String.format("Password: %s%nHash: %s%nMatches: %s", password, hash, matches);
    }
}
