package com.mikorsoft.milogdb.ui;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("name", "Student");
		model.addAttribute("pageTitle", "MiLogDB");
		model.addAttribute("headerTitle", "Welcome to MiLogDB");

		return "home";
	}

}
