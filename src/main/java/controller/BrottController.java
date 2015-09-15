package controller;

import javax.inject.Inject;

import model.Crime;
import model.CrimeHandler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import service.CrimesDAO;

@Controller
public class BrottController {

	private CrimeHandler crimeHandler;
		private CrimesDAO crimesDAO;

	@Inject
	public BrottController(CrimeHandler crimeHandler, CrimesDAO crimesDAO) {
		this.crimeHandler = crimeHandler;
		this.crimesDAO = crimesDAO;
		
	}

	@RequestMapping("/Brott")
	public String getCrime(Model model) {
		Crime crime = crimeHandler.getCrimeFromPolice();
		crime = crimeHandler.test(crime);
		
		crimesDAO.openConnection();
		crimesDAO.addCrime(crime);
		crimesDAO.closeConnection();
		
		model.addAttribute("crime", crime);
		return "index";
	}
	

}
