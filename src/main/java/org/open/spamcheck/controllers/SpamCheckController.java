package org.open.spamcheck.controllers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpamCheckController {
	
	@GetMapping("/checktest")
	public String testMethod() {
		return "Greetings from Spam Check !";
	}
	@RequestMapping(
			value = "/check",
			method = RequestMethod.POST,
			consumes = {"application/json"},
			produces = {"application/json"}
			)
	public String checkIfSpam() {
		return "You've entered Spam CHECK!";
	}
	
}
