package com.mikorsoft.milogdb.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class HomeController {

	@GetMapping("/home")
	public String home(Model model, Principal principal) {
		model.addAttribute("name", principal.getName());
		model.addAttribute("pageTitle", "MiLogDB");
		model.addAttribute("headerTitle", "Welcome to MiLogDB");

		return "home";
	}

}
