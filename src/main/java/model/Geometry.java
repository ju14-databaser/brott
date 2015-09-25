package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * WrapperClass for getting geoLocation ({@link Location})through JSON data from Googles Geocode
 * API. Two levels in, just above the location data that is wanted.
 * 
 * @author Lina
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Geometry {

	private Location location;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
}
