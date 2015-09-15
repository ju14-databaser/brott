package model;

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


	public Crime test(Crime crime){
		
		String stringToReadURLFrom = "https://maps.googleapis.com/maps/api/geocode/json?address="+ crime.getLocation();
		System.out.println(crime.getLocation());
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<AddressWrapper> entity = restTemplate.getForEntity(stringToReadURLFrom,
				AddressWrapper.class);
		Location location = entity.getBody().getResults()[0].getGeometry().getLocation();
		crime.setLatitud(location.getLat());
		crime.setLongitud(location.getLng());
		System.out.println(location);
		
		return crime;
	}


	private String getXMLCrime() {
		return null;
	}

}
