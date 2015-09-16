package model;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * GeoLocationParser uses Google API for geocodes to search geolocations
 * 
 * @author Lina
 *
 */
public class GeoLocationParser {

	private static final String HTTPS_GOOGLEAPIS_ADDRESS = "https://maps.googleapis.com/maps/api/geocode/json?address=";

	/**
	 * @param location
	 *            - should be an adress of some kind in the form of a String.
	 * @returns a Location wiht ingoing Latitud and Longitud
	 */
	public Location getGeoLocation(String location) {

		String stringToReadURLFrom = HTTPS_GOOGLEAPIS_ADDRESS + location;
		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<AddressWrapper> entity = restTemplate.getForEntity(stringToReadURLFrom,
					AddressWrapper.class);
			Location location2 = entity.getBody().getResults()[0].getGeometry().getLocation();
			return location2;
		} catch (RestClientException re) {
//			System.out.println("PROBLEM med Rest: " + re.getMessage());
			return null;
		} 
		catch (Exception e) {
			// TODO: bra errorhandling
			System.out.println("PROBLEM i geolocation, EJ REST: " + location);
//			System.out.println("ERROR: " + e.getMessage() + " " + e.getLocalizedMessage() + " "
//					+ e.toString());
			return null;
		}
	}
}
