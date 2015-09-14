package model;

import java.net.MalformedURLException;
import java.net.URL;

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

	public static void main(String[] args) {
		String stringToReadURLFrom = "https://maps.googleapis.com/maps/api/geocode/json?address=Stockholm,+Sweden";

		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<AddressWrapper> entity = restTemplate.getForEntity(stringToReadURLFrom,
				AddressWrapper.class);
		Location location = entity.getBody().getResults()[0].getGeometry().getLocation();
System.out.println(location);
	}

	private String getXMLCrime() {
		return null;
	}

}
