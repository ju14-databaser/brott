package model;

public class Crime {

	private String location;
	private String date;
	private String title;
	private String description;

	public Crime() {
	}

	public Crime(String title, String description) {
		this.title = title;
		this.description = description;
	}

	private void createLocation() {

	}

	private void createDate() {

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
}
