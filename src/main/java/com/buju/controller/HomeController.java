package com.buju.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.buju.entity.User;
import com.buju.repository.UserRepo;
import com.buju.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepo userRepo;

	@ModelAttribute
	public void commonUser(Principal p, Model m) {
		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}

	}

	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	/*
	 * @GetMapping("/user/profile") public String profile(Principal p, Model m) {
	 * String email = p.getName(); User user = userRepo.findByEmail(email);
	 * m.addAttribute("user", user); return "profile"; }
	 * 
	 * @GetMapping("/user/home") public String home() { return "home"; }
	 */

	@PostMapping("/saveUser")
	public String saveUser(@ModelAttribute User user, HttpSession session, Model m, HttpServletRequest request) {

		// System.out.println(user);
		String url = request.getRequestURL().toString();

		// System.out.println(url); http://localhost:8080/saveUser
		url = url.replace(request.getServletPath(), "");
		// System.out.println(url);
		// //http://localhost:8080/verify?code=3453sdfsdcsadcscd

		User u = userService.saveUser(user, url);

		if (u != null) { // System.out.println("save sucess");
			session.setAttribute("msg", "Registered successfully");

		} else { // System.out.println("error in server");
			session.setAttribute("msg", "Something went wrong");
		}

		return "redirect:/register";
	}

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model m) {
		boolean f = userService.verifyAccount(code);

		if (f) {
			m.addAttribute("msg", "Account verified successfully");
		} else {
			m.addAttribute("msg", "Incorrect verification code or account already verified");
		}

		return "message";
	}

}