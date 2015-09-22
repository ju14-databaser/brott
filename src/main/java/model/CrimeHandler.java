package model;

import java.util.List;
import java.util.function.Predicate;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import service.CrimesDAO;

@Component
public class CrimeHandler {

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

	public List<Crime> getNewCrimesFromPolice() {
		XMLParser xmlParser = new XMLParser();

		Crime latestCrime = crimesDAO.getLatestCrime();

		if (latestCrime.getTitle() == null) {
			return getAllCrimesFromPolice();
		}

		String latestCrimeTitle = latestCrime.getTitle();

		List<Crime> newCrimes = xmlParser.parseNewCrimes(latestCrimeTitle);
		// TODO: add geolocation
		List<Crimecategory> crimeCat = crimesDAO.getCrimeCategorys();
		newCrimes=setCrimeCategory(newCrimes, crimeCat);
		return newCrimes;

	}


	// @Scheduled(fixedDelay=600000)
	public void getAllCrimesFromPoliceScheduled() {
		List<Crime> allCrimesFromPolice = getAllCrimesFromPolice();

		if (allCrimesFromPolice.size() > 0) {
			LOGGER.error("LYCKADES UPPDATERA");
		} else {
			LOGGER.error("MISSLYCKADES MED ATT HÄMTA");
		}
	}

	public List<Crime> getAllCrimesFromPolice() {
		XMLParser xmlParser = new XMLParser();

		List<Crime> crimes = xmlParser.parseAllCrimes();
		List<Crimecategory> crimeCat = crimesDAO.getCrimeCategorys();
		if (crimes.size() > 0) {

			crimes = setCrimeCategory(crimes, crimeCat);

		}
		return crimes;
	}

	/**
	 * Method that tries to find the category of the crime by searching through the crimecategory table.
	 * If no category was found, it sets it to "Övrigt" 
	 * 
	 * @param crimes	list of crimes
	 * @param crimeCat	all categorys
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
		for(Crime crime: crimes){
			foundCat = false;
			
			crime.setGeoLocation(geoLocationParser.getGeoLocation(crime.getLocation()));

			for (int i = 0; i < crimeCat.size(); i++) {

				if (crime.getCategory().toLowerCase().contains(crimeCat.get(i).getCategory().toLowerCase())) {
					crime.setCrimecategory(crimeCat.get(i));
					foundCat = true;
					break;
				}
			}
			
			if (foundCat==false) {
				crime.setCrimecategory(notFound);
			}
			

		}
		return crimes;
	}

	@Scheduled(fixedDelay = 300000)
	public void updateGeoLocationsScheduled() {
		updateGeoLocations();

	}

	public int updateGeoLocations() {
		int updated = 0;
		LOGGER.error("Nu ska här uppdateras GEOLOCATIONS!");
		List<Crime> allCrimes = crimesDAO.getAllCrimes();
		crimesDAO.openConnection();

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

}
