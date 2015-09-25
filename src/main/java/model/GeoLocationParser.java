package model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import model.JsonWrapper.AddressWrapper;
import model.JsonWrapper.AdressResultsWrapper;

/**
 * GeoLocationParser uses Google API for geocodes to search geolocations
 * 
 * @author Lina
 *
 */
public class GeoLocationParser {

	static final String HTTPS_GOOGLEAPIS_ADDRESS = "https://maps.googleapis.com/maps/api/geocode/json?address=";
	private static final Logger LOGGER = LoggerFactory.getLogger(GeoLocationParser.class);

	/**
	 * @param location
	 *            - should be an adress of some kind in the form of a String.
	 * @returns a Location wiht ingoing Latitud and Longitud
	 */
	public Location getGeoLocation(String location) {

		String stringToReadURLFrom = HTTPS_GOOGLEAPIS_ADDRESS + location;
		return parseGeoData(stringToReadURLFrom);
	}

	Location parseGeoData(String stringToReadURLFrom) {
		RestTemplate restTemplate = new RestTemplate();

		try {
			ResponseEntity<AddressWrapper> entity = restTemplate.getForEntity(stringToReadURLFrom,
					AddressWrapper.class);
			AdressResultsWrapper[] results = entity.getBody().getResults();
			if (results.length == 0) {
				LOGGER.error("No geolocation found for the location in Google. Returning null.");
				return null;
			}
			Location location2 = results[0].getGeometry().getLocation();
			return location2;
		} catch (RestClientException re) {
			LOGGER.error("Problems with the rest service for getting geolocation as JSON data. "
					+ re.getMessage(), re);
			return null;
		} catch (Exception e) {
			LOGGER.error(
					"Problems getting geolocation as JSON data. Not related to Rest "
							+ e.getMessage(), e);
			return null;
		}
	}

}
