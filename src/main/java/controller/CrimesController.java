package controller;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import model.Crime;
import model.CrimeFacade;
import model.Crimecategory;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Spring MVC Controller for the Brott web-applikation. Using annotations in
 * connection with spring-beans to handle the web-requests.
 *@author Lina, Anna, Erik
 *
 */
@Controller
public class CrimesController {

	private CrimeFacade crimeFacade;

	/**
	 * Constructor for the Spring MVC Controller. Uses autowiring through @Inject
	 * at start-up.
	 * 
	 * @param crimeHandler
	 *            The Crimehandler that is a facade to the rest of the
	 *            application. Autowired through spring-bean and annotations for
	 *            startup.
	 */
	@Inject
	public CrimesController(CrimeFacade crimeHandler) {
		this.crimeFacade = crimeHandler;

	}

	/**
	 * Method for requestMapping for the Brott/Brott site. Will try to fetch
	 * data from the datasource. Will add all crimes and crimecategories as
	 * attributes to the parameter Model.
	 * 
	 * @param model
	 *            from the web-request. Picked up by Spring framework.
	 * @return "index" a String representing the index.jsp page
	 */
	@RequestMapping("/brottkarta")
	public String getCrime(Model model) {
		List<Crime> allCrimes = crimeFacade.getAllCrimesFromDB();
		List<Crimecategory> crimecat = crimeFacade.getAllCategorysFromDB();

		model.addAttribute("Crimecat", crimecat);
		model.addAttribute("Crimes", allCrimes);
		return "index";
	}

	/**
	 * Method for requestMapping Brott/New, will update datasource and take into
	 * consideration what crimes already are in the database. Updates the
	 * datasource with all new crimes from the police RSS-feed. Will add numbers
	 * for how many crimes were added as attributes to the model.
	 * 
	 * Due to problems with the specific RSS-feed that the Police has this is
	 * only possible to do once per application run. Suggestion is that if this
	 * application is uploaded to a web-server this method should be placed in a
	 * separate jar with a scheduled job on the server. So that the server
	 * starts the method and updates the database.
	 * 
	 * @param model
	 *            from the web-request. Picked up by Spring framework.
	 * @return "allCrimes" a String representing the alLCrimes.jsp page
	 */
	//ANNA 
	@RequestMapping("/New")
	public String loadDatabaseWithNewCrimes(Model model) {
		List<Crime> allCrimesFromPolice = crimeFacade.getNewCrimesFromPolice();
		crimeFacade.writeCrimesToDB(allCrimesFromPolice);

		model.addAttribute("crimeNo", allCrimesFromPolice.size());
		model.addAttribute("crimes", allCrimesFromPolice);
		return "allCrimes";
	}

	/**
	 * Method for requestMapping Brott/load, will fill the dataource with all
	 * crimes from the Rss-feed. Updates the datasource with all crimes from the
	 * police RSS-feed. Will add numbers for how many crimes were added as
	 * attributes to the model.
	 * 
	 * This method should be used when you have an empty datbase that you want
	 * to fill without taking into consideration if the crimes already are there
	 * or not.
	 * 
	 * Due to problems with the specific RSS-feed that the Police has this is
	 * only possible to do once per application run. Suggestion is that if this
	 * application is uploaded to a web-server this method should be placed in a
	 * separate jar with a scheduled job on the server. So that the server
	 * starts the method and updates the database.
	 * 
	 * 
	 * @param model
	 *            from the web-request. Picked up by Spring framework.
	 * @return "allCrimes" a String representing the alLCrimes.jsp page
	 */
	@RequestMapping("/load")
	public String loadDatabaseWithAllCrimes(Model model) {
		List<Crime> allCrimesFromPolice = crimeFacade.getAllCrimesFromPolice();
		crimeFacade.writeCrimesToDB(allCrimesFromPolice);

		model.addAttribute("crimeNo", allCrimesFromPolice.size());
		model.addAttribute("crimes", allCrimesFromPolice);
		return "allCrimes";
	}

	/**
	 * Method for requestMapping Brott/update, will update geolocations on 10
	 * entries in the datasource where these are missing.
	 * 
	 * @param model
	 *            from the web-request. Picked up by Spring framework.
	 * @return will call the showDBstatistics method and from there get the
	 *         "dbInfo.jsp" page.
	 */
	@RequestMapping("/update")
	public String updateGeoLocationsFor10Entries(Model model) {
		model.addAttribute("updated", crimeFacade.updateGeoLocations());

		return showDBStatistics(model);
	}

	/**
	 * Method for requestMapping Brott/statistik, will show information about
	 * the datasource. This method is for administrative causes. To enable
	 * easier management of the datasource.
	 * 
	 * @param model
	 *            from the web-request. Picked up by Spring framework.
	 * @return "dbInfo" a String representing the "dbInfo.jsp" page.
	 */
	@RequestMapping("/admin")
	public String showDBStatistics(Model model) {
		List<Crime> allCrimes = crimeFacade.getAllCrimesFromDB();

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

	/**
	 * Method that will catch if there is a RuntimeException and re-direct to
	 * the error.jsp page.
	 * 
	 * @return "error" a String representation of the error.jsp page
	 */
	@ExceptionHandler(RuntimeException.class)
	public String databaseConnectionError() {
		return "error";
	}

	/**
	 * Method that will catch if there is a RuntimeException and re-direct to
	 * the error.jsp page.
	 * 
	 * @return "error" a String representation of the error.jsp page
	 */
	@ExceptionHandler(IOException.class)
	public String rssFeedError() {
		return "error";
	}

}
