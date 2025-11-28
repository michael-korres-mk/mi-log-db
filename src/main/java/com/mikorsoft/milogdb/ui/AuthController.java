package com.mikorsoft.milogdb.ui;

import com.mikorsoft.milogdb.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;
	private final AuthenticationManager authenticationManager;

	@GetMapping("/")
	public String auth(@RequestParam(required = false, defaultValue = "0") Integer mode, Model model) {
		System.out.println("Selected mode: " + mode); // 0 or 1
		model.addAttribute("mode", mode);
		model.addAttribute("pageTitle", "MiLogDB");
		model.addAttribute("headerTitle", "Welcome to MiLogDB");

		return "auth";
	}

	@PostMapping("/signup")
	public String login(@RequestParam String username, @RequestParam String password, Model model, HttpServletRequest request) {

		System.out.println(username);
		System.out.println(password);

		int inserted = userRepository.insert(username, password);

		if (inserted == 0) {
			System.out.println("USER ALREADY EXISTS");
			model.addAttribute("signupError", "Username already taken");
			model.addAttribute("mode", 1);
			return "auth";
		}
		else{
			// TODO: Clear these
			username = "user";
			password = "password";
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
			Authentication auth = authenticationManager.authenticate(authToken);

			// Persist in the SecurityContext
			SecurityContext context = SecurityContextHolder.createEmptyContext();
			context.setAuthentication(auth);
			SecurityContextHolder.setContext(context);

			// Store it in HTTP session so it survives redirects
			HttpSession session = request.getSession(true);
			session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);

			return "redirect:/home"; // hits the HomeController
		}

	}
}
