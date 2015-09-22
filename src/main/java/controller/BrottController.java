package controller;

import java.util.List;

import javax.inject.Inject;

import model.Crime;
import model.CrimeHandler;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

		List<Crime> allCrimes = crimeHandler.getAllCrimesFromDB();

		model.addAttribute("Crimes", allCrimes);
		return "index";
	}

	@RequestMapping("/New")
	public String loadDatabaseWithNewCrimes(Model model) {
		List<Crime> allCrimesFromPolice = crimeHandler.getNewCrimesFromPolice();
		crimeHandler.writeCrimesToDB(allCrimesFromPolice);

		model.addAttribute("crimeNo", allCrimesFromPolice.size());
		model.addAttribute("crimes", allCrimesFromPolice);
		return "allCrimes";
	}

	@RequestMapping("/load")
	public String loadDatabaseWithAllCrimes(Model model) {
		List<Crime> allCrimesFromPolice = crimeHandler.getAllCrimesFromPolice();
		crimeHandler.writeCrimesToDB(allCrimesFromPolice);

		model.addAttribute("crimeNo", allCrimesFromPolice.size());
		model.addAttribute("crimes", allCrimesFromPolice);
		return "allCrimes";
	}

	@RequestMapping("/update")
	public String updateGeoLocationsFor10Entries(Model model) {
		model.addAttribute("updated", crimeHandler.updateGeoLocations());

		return showDBStatistics(model);
	}

	@RequestMapping("/statistik")
	public String showDBStatistics(Model model) {
		List<Crime> allCrimes = crimeHandler.getAllCrimesFromDB();

		model.addAttribute("noRows", allCrimes.size());

		int allEmptyCrimes = 0;

		for (Crime crime : allCrimes) {
			if (crime.getGeoLocation() == null) {
				allEmptyCrimes++;
			}
		}

		model.addAttribute("noEmpty", allEmptyCrimes);

		return "dbInfo";
	}

	@ExceptionHandler(RuntimeException.class)
	public String databaseConnectionError(){
		return "dbError";
	}
	
}
