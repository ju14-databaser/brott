package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class Crime {

	private static final Date ERROR_DATE = new Date(0);
	private String location;
	private String date;
	private String title;
	private String description;
	private String crimeCat;
	private Location geoLocation;
	private Date dateStamp;

	public String getCrimeCat() {
		return crimeCat;
	}

	public void setCrimeCat(String crimeCat) {
		this.crimeCat = crimeCat;
	}

	public Location getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(Location geoLocation) {
		this.geoLocation = geoLocation;
	}

	public Crime() {
	}

	public Crime(String title, String description) {
		 
		this.title = title.replace("\r","").replace("\n","");
		this.description = description.replace("\r","").replace("\n","");
		createLocation();
		createDate();
	}

	private void createLocation() {
		int i = description.indexOf('.');
		this.location = this.description.substring(0, i) + ", Sweden";
	}

	/**
	 * Will populate the dateStamp with a date from the beginning of the title
	 * string. The date will only capture the Date, not the Time
	 * 
	 * If ParseException is caught, the time will be set to Jan 1st 1970.
	 */
	private void createDate() {

		this.date = title.substring(0, title.indexOf(","));
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

		try {
			setDateStamp(df.parse(date));
		} catch (ParseException e) {
			setDateStamp(ERROR_DATE);
		}
	}

	public String getLocation() {
		return location;
	}

	public String getDate() {
		return date;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}


	public Date getDateStamp() {
		return dateStamp;
	}

	public void setDateStamp(Date dateStamp) {
		this.dateStamp = dateStamp;
	}

}
