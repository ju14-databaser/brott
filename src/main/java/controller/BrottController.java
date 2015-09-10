package controller;

import model.Crime;
import model.CrimeHandler;


public class BrottController {
	
	public String getCrime(Model model){
		CrimeHandler crimeHandler = new CrimeHandler();
		
		Crime crime = crimeHandler.getCrimeFromPolice();
		model.addAttribute("crime",crime);
		return "home";
	}

}
