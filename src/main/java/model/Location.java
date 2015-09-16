package model;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class Location {

	private String lat;
	private String lng;

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

	public boolean isEmpty() {
		if (lat == null && lng == null) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Latitud: " + lat + " Longitud: " + lng;
	}

}
