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

@Component
@EnableScheduling
public class CrimeHandler {

	private final static String POLICE_RSS = "https://polisen.se/Aktuellt/RSS/Lokal-RSS---Handelser/Lokala-RSS-listor1/Handelser-RSS---Stockholms-lan/?feed=rss";
	private static final Logger LOGGER = LoggerFactory.getLogger(CrimeHandler.class);
	private GeoLocationParser geoLocationParser;
	private CrimesDAO crimesDAO;

	public CrimeHandler() {
	}

	@Inject
	public CrimeHandler(CrimesDAO crimesDAO) {
		this.crimesDAO = crimesDAO;
		geoLocationParser = new GeoLocationParser();
	}

	// TODO: Går det att avbryta om det tar för lång tid?
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
		List<Crimecategory> crimeCat = crimesDAO.getCrimeCategorys();
		newCrimes = setCrimeCategory(newCrimes, crimeCat);
		return newCrimes;

	}

	// @Scheduled(fixedDelay=600000)
	public void getAllCrimesFromPoliceScheduled() {
		List<Crime> allCrimesFromPolice = getAllCrimesFromPolice();

		if (allCrimesFromPolice.size() > 0) {
			LOGGER.debug("LYCKADES UPPDATERA");
		} else {
			LOGGER.error("MISSLYCKADES MED ATT HÄMTA");
		}
	}

	public void writeCrimesToDB(List<Crime> crimesToAdd) {
		crimesDAO.openConnection();
		crimesToAdd.forEach(crime -> {
			crimesDAO.addCrime(crime);
		});
		crimesDAO.closeConnection();
	}

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
		}

		List<Crimecategory> crimeCat = crimesDAO.getCrimeCategorys();
		if (crimes.size() > 0) {

			crimes = setCrimeCategory(crimes, crimeCat);

		}
		return crimes;
	}

	/**
	 * Method that tries to find the category of the crime by searching through
	 * the crimecategory table. If no category was found, it sets it to "Övrigt"
	 * 
	 * @param crimes
	 *            list of crimes
	 * @param crimeCat
	 *            all categorys
	 * @return list of crimes with categorys
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
		for (Crime crime : crimes) {
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

	@Scheduled(fixedDelay = 300000, initialDelay = 300000)
	public void updateGeoLocationsScheduled() {
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
					crimesDAO.updateCrime(crime, geoLocation);
					updated++;
				}

			}

			if (updated == 10) {
				crimesDAO.closeConnection();
				return updated;

			}

		}
		crimesDAO.closeConnection();
		return updated;
	}

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
	
	public List<Crimecategory> getAllCategorysFromDB(){
		List<Crimecategory> crimecat;
		try {
		crimecat=crimesDAO.getCrimeCategorys();
		}catch(PersistenceException e){
			LOGGER.error("Error when connecting to the database for getting all categorys. "
					+ e.getMessage());
			throw new RuntimeException(
					"Error when connecting to the database for getting all categorys. "
					+ e.getMessage());
		}
		return crimecat;
	}

}
