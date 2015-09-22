package model;

import java.io.Serializable;
import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;


/**
 * The persistent class for the CRIMES database table.
 * 
 */
@Entity
@Table(name="CRIMES", schema="STOCKHOLM")
public class Crime implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final Date ERROR_DATE = new Date(0);

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	private String location;

	@Transient
	private String date;
	private String title;
	private String description;
	
	@Embedded
	private Location geoLocation;
	private Date dateStamp;

	

	//bi-directional many-to-one association to Crimecategory
	@ManyToOne
	@JoinColumn(name="CRIMECAT_ID")
	private Crimecategory crimecategory;

	public Crime() {
	}

	public Crime(String title, String description) {

		this.title = title.replace("\r", "").replace("\n", "").trim();
		this.description = description.replace("\r", "").replace("\n", "").trim();
		createLocation();
		createDate();
	}
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Crimecategory getCrimecategory() {
		return this.crimecategory;
	}

	public void setCrimecategory(Crimecategory crimecategory) {
		this.crimecategory = crimecategory;
	}
	public String getCategory(){
		int i = title.indexOf(",");
		int j = title.lastIndexOf(",");
		
		return this.title.substring(i, j);
	}

	private void createLocation() {
		int i = description.indexOf('.');
		if (i == -1) {
			i = description.length()-1;
		}
		
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
	
	public Date getDateStamp() {
		return dateStamp;
	}

	public void setDateStamp(Date dateStamp) {
		this.dateStamp = dateStamp;
	}
	
	public Location getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(Location geoLocation) {
		this.geoLocation = geoLocation;
	}
	
}