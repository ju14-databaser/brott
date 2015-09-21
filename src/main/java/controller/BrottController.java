package controller;

import java.util.List;

import javax.inject.Inject;

import model.Crime;
import model.CrimeHandler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import service.CrimesDAO;

@Controller
@EnableScheduling
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

		List<Crime> allCrimes = crimesDAO.getAllCrimes();

		model.addAttribute("Crimes", allCrimes);
		return "index";
	}
	
	@RequestMapping("/New")
	public String loadDatabaseWithNewCrimes(Model model) {
		List<Crime> allCrimesFromPolice = crimeHandler.getNewCrimesFromPolice();
		allCrimesFromPolice.forEach(crime -> {
			crimesDAO.openConnection();
			crimesDAO.addCrime(crime);
			crimesDAO.closeConnection();
		});

		model.addAttribute("crimeNo", allCrimesFromPolice.size());
		model.addAttribute("crimes", allCrimesFromPolice);
		return "allCrimes";
	}
	

	@RequestMapping("/load")
	// TODO: Kolla spring-batch eller liknande om man vill schemalägga
	public String loadDatabaseWithAllCrimes(Model model) {
		List<Crime> allCrimesFromPolice = crimeHandler.getAllCrimesFromPolice();
		allCrimesFromPolice.forEach(crime -> {
			crimesDAO.openConnection();
			crimesDAO.addCrime(crime);
			crimesDAO.closeConnection();
		});

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
		List<Crime> allCrimes = crimesDAO.getAllCrimes();

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

}
