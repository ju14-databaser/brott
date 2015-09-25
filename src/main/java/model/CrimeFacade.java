package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import service.CrimesDAO;
/**
 * Class for handling the crimes.
 * Has a scheduled job for updating geoLocations.
 * 
 * @author Erik, Lina, Anna
 *
 */
@Component
@EnableScheduling
public class CrimeFacade {

	private final static String POLICE_RSS = "https://polisen.se/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Stockholms-lan/?feed=rss";
	private static final Logger LOGGER = LoggerFactory.getLogger(CrimeFacade.class);
	private GeoLocationParser geoLocationParser;
	private CrimesDAO crimesDAO;
	private boolean allHasGeoLocation;

	public CrimeFacade() {
	}

	/**
	 * Constructor of the class. Uses autowiring through @Inject
	 * Initializes the CrimesDAO class, geoLocationParser and prepares 
	 * the allHasGeoLocation flag for the scheduled geoLocation job.
	 * 
	 * @param crimesDAO The class which is handling database operations.
	 */
	@Inject
	public CrimeFacade(CrimesDAO crimesDAO) {
		this.crimesDAO = crimesDAO;
		geoLocationParser = new GeoLocationParser();
		allHasGeoLocation=false;
	}

	/**
	 * This method reads new crimes from the police rss-feed. If the database doesn't 
	 * contain any crimes it calls the getAllCrimesFromPolice() method which 
	 * reads the whole rss-feed. If the table contains crimes,
	 * it takes the latest crime from the table and reads the rss-feed until the latest crime has been matched.
	 * Inserts the new crimes into the table.
	 * 
	 * @return The list of new crimes
	 */
	public List<Crime> getNewCrimesFromPolice() {
		XMLParser xmlParser = new XMLParser(POLICE_RSS);
		Crime latestCrime;
		try {
			latestCrime = crimesDAO.getLatestCrime();
		} catch (PersistenceException e) {
			LOGGER.error("Error when connecting to the database for getting new crimes. "
					+ e.getMessage());
			throw new RuntimeException(
					"Error when connecting to the database for getting new crimes. "
							+ e.getMessage());
		}

		if (latestCrime.getTitle() == null) {
			return getAllCrimesFromPolice();
		}

		String latestCrimeTitle = latestCrime.getTitle();

		List<Crime> newCrimes;
		try {
			newCrimes = xmlParser.parseNewCrimes(latestCrimeTitle);
		} catch (SAXException e) {
			LOGGER.error("Error in parsing the RSS feed from the Police_RSS. " + e.getMessage());
			newCrimes = new ArrayList<>();
			return newCrimes;
		} catch (IOException e) {
			LOGGER.error("Connection error when trying to access the Police rss feed. "
					+ e.getMessage());
			newCrimes = new ArrayList<>();
			return newCrimes;
		}
		// TODO: add geolocation
		allHasGeoLocation=false;
		List<Crimecategory> crimeCat = crimesDAO.getCrimeCategorys();
		newCrimes = setCrimeCategory(newCrimes, crimeCat);
		return newCrimes;

	}

	/**
	 * This method is called to insert crimes into the database.
	 * Calls the addCrime method in CrimesDAO which inserts each crime into the table.
	 * 
	 * @param crimesToAdd The list of new Crimes
	 */
	public void writeCrimesToDB(List<Crime> crimesToAdd) {
		crimesDAO.openConnection();
		crimesToAdd.forEach(crime -> {
			crimesDAO.addCrime(crime);
		});
		crimesDAO.closeConnection();
	}

	/**
	 * This method calls the parseAllCrimes() in XMLParser to get a list of crimes from the police rss-feed.
	 * Calls the getCrimeCategorys in the crimesDAO class which fetches all the crime-categorys from our static
	 * Crimecategory table. Calls the method setCrimeCategory which handles the mapping between crimes and categorys.
	 * 
	 * @return The list of crimes which now has a category mapped to each crime. 
	 */
	public List<Crime> getAllCrimesFromPolice() {
		XMLParser xmlParser = new XMLParser(POLICE_RSS);

		List<Crime> crimes;
		try {
			crimes = xmlParser.parseAllCrimes();
		} catch (SAXException e) {
			LOGGER.error("Error in parsing the RSS feed from the Police_RSS. " + e.getMessage(), e);
			crimes = new ArrayList<>();
			return crimes;
		} catch (IOException e) {
			LOGGER.error(
					"Connection error when trying to access the Police rss feed. " + e.getMessage(),
					e);
			crimes = new ArrayList<>();
			return crimes;
			//TODO: Är detta vad vi vill göra? Hur vill vi hantera det för användaren här?
		}

		List<Crimecategory> crimeCat = crimesDAO.getCrimeCategorys();
		if (crimes.size() > 0) {

			crimes = setCrimeCategory(crimes, crimeCat);

		}
		allHasGeoLocation= false;
		return crimes;
	}

	/**
	 * Method that tries to find the category of the crime by searching through
	 * the crimecategory table. Compares the crime-category read from the rss-feed to our crime-categorys from our table. 
	 * If no category was found, it sets it to "Övrigt".
	 * 
	 * @param crimes
	 *            list of crimes without categorys
	 * @param crimeCat
	 *            all categorys
	 * @return list of crimes with category's
	 */
	public List<Crime> setCrimeCategory(List<Crime> crimes, List<Crimecategory> crimeCat) {
		// Förbereder ett Crimecategory med "Övrigt" som categori
		Crimecategory notFound = null;
		for (Crimecategory cat : crimeCat) {
			if (cat.getCategory().equalsIgnoreCase("Övrigt")) {
				notFound = cat;
			}
		}
		boolean foundCat;
		for(Crime crime : crimes){
			foundCat = false;
			
			crime.setGeoLocation(geoLocationParser.getGeoLocation(crime.getLocation()));

			for (int i = 0; i < crimeCat.size(); i++) {

				if (crime.getCategory().toLowerCase()
						.contains(crimeCat.get(i).getCategory().toLowerCase())) {
					crime.setCrimecategory(crimeCat.get(i));
					foundCat = true;
					break;
				}
			}
			
			if (foundCat == false) {
				crime.setCrimecategory(notFound);
			}
			

		}
		return crimes;
	}

	/**
	 * A scheduled job which checks for crimes in our table, which are dont have geoLocations.
	 * Calls the updateGeolocations() which tries to get the coordinates from the google geocode-api.
	 * Only does this if the last job didn't succeed in updating all locations.
	 */
	@Scheduled(fixedDelay = 60000, initialDelay = 60000)
	public void updateGeoLocationsScheduled() {

		if (allHasGeoLocation) {
			return;
		}
		LOGGER.debug("Starting scheduled job for updating geolocations in database.");
		int noUpdated = 0;
		try {
			noUpdated = updateGeoLocations();
		} catch (RuntimeException e) {
			LOGGER.error(
					"Error when connecting to the database for scheduled job for updating geolocations. "
							+ e.getMessage(), e);
		}
		LOGGER.debug("Finished scheduled job for updating geolocations in database. Updated "
				+ noUpdated + " geolocations");
	}

	/**
	 * This method fetches all crimes from the table and calls the geoLocationParser to fetch coordinates for 
	 * crimes without geoLocations.
	 * If coordinates were found, it updates the crime in the table.
	 * If the limit of 10 updates per job has been reached, returns the amount of updated crimes.
	 * 
	 * @return The amount of crimes that were updated.
	 */
	public int updateGeoLocations() {
		int updated = 0;
		List<Crime> allCrimes = crimesDAO.getAllCrimes();
		try {
			crimesDAO.openConnection();
		} catch (PersistenceException e) {
			LOGGER.error("Error when connecting to the database for updating geolocations. "
					+ e.getMessage());
			throw new RuntimeException(
					"Error when connecting to the database for updating geolocations. "
							+ e.getMessage(), e);
		}

		for (Crime crime : allCrimes) {

			if (crime.getGeoLocation() == null) {

				Location geoLocation = geoLocationParser.getGeoLocation(crime.getLocation());

				if (geoLocation != null) {
					crimesDAO.updateCrimeGeoLocation(crime, geoLocation);
					updated++;
				}

			}

			if (updated == 10) {
				crimesDAO.closeConnection();
				return updated;

			}

		}
		allHasGeoLocation = true;
		crimesDAO.closeConnection();
		return updated;
	}

	/**
	 * Fetches all the crimes from the table by calling the getAllCrimes() method in CrimesDAO class.
	 * 
	 * @return The list of crimes from the table.
	 */
	public List<Crime> getAllCrimesFromDB() {

		List<Crime> allCrimes;
		try {
			allCrimes = crimesDAO.getAllCrimes();
		} catch (PersistenceException e) {
			LOGGER.error("Error when connecting to the database for getting new crimes. "
					+ e.getMessage());
			throw new RuntimeException(
					"Error when connecting to the database for getting new crimes. "
							+ e.getMessage());
		}
		return allCrimes;
	}

	/**
	 * Fetches all the crime-category's from the table by calling the getCrimeCategorys method in crimesDAO class. 
	 * 
	 * @return The list of category's from the table.
	 */
	public List<Crimecategory> getAllCategorysFromDB() {
		List<Crimecategory> crimecat;
		try {
			crimecat = crimesDAO.getCrimeCategorys();
		} catch (PersistenceException e) {
			LOGGER.error("Error when connecting to the database for getting all categorys. "
					+ e.getMessage());
			throw new RuntimeException(
					"Error when connecting to the database for getting all categorys. "
							+ e.getMessage());
		}
		return crimecat;
	}
	
	/**
	 * 
	 * @return the flag which checks if all the crimes have geoLocation.
	 */
	public boolean isAllHasGeoLocation() {
		return allHasGeoLocation;
	}

}
