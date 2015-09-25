package model.JsonWrapper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * WrapperClass for getting geoLocation ({@link Location}) through JSON data from Googles Geocode
 * API. One level in.
 * 
 * @author Lina
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdressResultsWrapper {

	private Geometry geometry;

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

}
