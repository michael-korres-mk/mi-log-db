package com.mikorsoft.milogdb.ui;

import com.mikorsoft.milogdb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;

	@GetMapping("/")
	public String auth(@RequestParam(required = false, defaultValue = "0") Integer mode, Model model) {
		System.out.println("Selected mode: " + mode); // 0 or 1
		model.addAttribute("mode", mode);
		model.addAttribute("pageTitle", "MiLogDB");
		model.addAttribute("headerTitle", "Welcome to MiLogDB");

		return "auth";
	}

	@PostMapping("/signup")
	public String login(@RequestParam String username,@RequestParam String password, Model model) {

		System.out.println(username);
		System.out.println(password);

		int inserted = userRepository.insert(username, password);

		if (inserted == 0) {
			System.out.println("USER ALREADY EXISTS");
			model.addAttribute("signupError", "Username already taken");
			model.addAttribute("mode", 1);
			return "auth";
		}

		model.addAttribute("name", username);
		return "home";
	}
}
