package model;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CrimeHandler {

	public Crime getCrimeFromPolice() {

		String xml = getXMLCrime();
		XMLParser xmlParser = new XMLParser();

		Crime crime = xmlParser.parseTOCrime(xml);

		return crime;

	}

	public List<Crime> getAllCrimesFromPolice() {
		XMLParser xmlParser = new XMLParser();
		return xmlParser.parseAllCrimes();
	}

	public Crime getGeoLocation(Crime crime) {

		String stringToReadURLFrom = "https://maps.googleapis.com/maps/api/geocode/json?address="
				+ crime.getLocation();
		System.out.println(crime.getLocation());
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<AddressWrapper> entity = restTemplate.getForEntity(stringToReadURLFrom,
				AddressWrapper.class);
		Location location = entity.getBody().getResults()[0].getGeometry().getLocation();
		crime.setGeoLocation(location);
		System.out.println(location);

		return crime;
	}

	
	//TODO: Flytta till egen klass, har inte med crimes att göra
	public Location addGeoLocation(String location) {

		String stringToReadURLFrom = "https://maps.googleapis.com/maps/api/geocode/json?address="
				+ location;
		System.out.println(location);
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<AddressWrapper> entity = restTemplate.getForEntity(stringToReadURLFrom,
				AddressWrapper.class);
		
		try {
			return entity.getBody().getResults()[0].getGeometry().getLocation();
		} catch (Exception e) {
			// TODO: bra errorhandling
			System.out.println("PROBLEM i geolocation: " + location);
			return null;
		}
	}

	private String getXMLCrime() {
		return null;
	}

}
