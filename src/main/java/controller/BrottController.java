package controller;

import javax.inject.Inject;

import model.Crime;
import model.CrimeHandler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BrottController {

	private CrimeHandler crimeHandler;

	@Inject
	public BrottController(CrimeHandler crimeHandler) {
		this.crimeHandler = crimeHandler;
	}

	@RequestMapping("/Brott")
	public String getCrime(Model model) {

		System.out.println("version2");
		Crime crime = crimeHandler.getCrimeFromPolice();
		model.addAttribute("crime", crime);
		return "index";
	}

}
