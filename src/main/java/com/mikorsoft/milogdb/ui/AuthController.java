package com.mikorsoft.milogdb.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class AuthController {

	@GetMapping("/")
	public String auth(@RequestParam(required = false) Integer mode, Model model) {

		System.out.println("Selected mode: " + ((mode == null)? 0 : mode)); // 0 or 1
		model.addAttribute("mode", (mode == null)? 0 : mode);

		model.addAttribute("pageTitle", "MiLogDB");
		model.addAttribute("headerTitle", "Welcome to MiLogDB");

		return "auth";
	}

	@GetMapping("/users")
	public String login(@RequestParam String username,@RequestParam String password, Model model) {

		System.out.println(username);
		System.out.println(password);


		model.addAttribute("name", username);
		return "home";
	}
}
