package model;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import service.CrimesDAO;

@Component
public class CrimeHandler {

	private GeoLocationParser geoLocationParser;
	private CrimesDAO crimesDAO;

	public CrimeHandler() {
	}

	@Inject
	public CrimeHandler(CrimesDAO crimesDAO) {
		this.crimesDAO = crimesDAO;
		geoLocationParser = new GeoLocationParser();
	}

	public Crime getLatestCrimeFromPolice() {

		String xml = getXMLCrime();
		XMLParser xmlParser = new XMLParser();

		Crime crime = xmlParser.parseTOCrime(xml);
		crime.setGeoLocation(geoLocationParser.getGeoLocation(crime.getLocation()));

		return crime;

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

	public int updateGeoLocations() {
		int updated = 0;

		List<Crime> allCrimes = crimesDAO.getAllCrimes();
		crimesDAO.openConnection();
		
		for (Crime crime : allCrimes) {

			if (crime.getGeoLocation().isEmpty()) {
				
				Location geoLocation = geoLocationParser.getGeoLocation(crime.getLocation());
				
				if (!geoLocation.isEmpty()) {
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
