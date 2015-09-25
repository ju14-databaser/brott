package model;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A representation of geographic co-ordinates. Latitud is covered by lat and
 * Longitud by lng. Corresponds to part of the JSON data that is possible to
 * retrieve from Googles Geocode API.
 * 
 * See wrapperClasses {@link AddressWrapper}, {@link AdressResultsWrapper},
 * {@link Geometry} for use with {@link GeoLocationParser}
 * 
 * @author Lina
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Location {

	private String lat;
	private String lng;

	/**
	 * Default constructor to enable use of empty object.
	 */
	public Location() {
	}

	/**
	 * Constructor for setting lat and lng at creation.
	 * 
	 * @param lat
	 *            Latitud
	 * @param lng
	 *            Longitud
	 */
	public Location(String lat, String lng) {
		this.lat = lat;
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	/**
	 * method for checking if this is an empty object
	 * 
	 * @return true if there are neither lat nor lng set in the object. False
	 *         otherwise
	 */
	public boolean isEmpty() {
		if (lat == null && lng == null) {
			return true;
		}
		return false;
	}

	/**
	 * @return "Latitud: " + lat + " Longitud: " + lng
	 */
	@Override
	public String toString() {
		return "Latitud: " + lat + " Longitud: " + lng;
	}

}
