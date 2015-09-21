package model;

import java.util.List;

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
		return newCrimes;

	}

	public Crime getLatestCrimeFromPolice() {

		String xml = getXMLCrime();
		XMLParser xmlParser = new XMLParser();

		Crime crime = xmlParser.parseTOCrime(xml);
		crime.setGeoLocation(geoLocationParser.getGeoLocation(crime.getLocation()));

		return crime;

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
		if (crimes.size() > 0) {
			crimes.forEach(crime -> crime.setGeoLocation(geoLocationParser.getGeoLocation(crime
					.getLocation())));
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

	private String getXMLCrime() {
		return null;
	}

}
