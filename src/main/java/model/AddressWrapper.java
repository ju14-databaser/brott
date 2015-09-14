package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressWrapper {

	private AdressResultsWrapper[] results;
	private int totalResults;

	public AdressResultsWrapper[] getResults() {
		return results;
	}

	public void setResults(AdressResultsWrapper[] results) {
		this.results = results;
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

}
