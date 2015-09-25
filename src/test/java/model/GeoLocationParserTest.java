package model;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;

public class GeoLocationParserTest {

	private String jsonFile;
	private URI uri;

	@Before
	public void setup() {
		jsonFile = "src/test/resources/adressGoogleGeoJson.json";
	}

	@Test
	public void adressSentInForParsing_correctLocationIsCreated() {

		Location expectedLocation = new Location("59.39845440000001", "17.8707829");
		String adressToSearch = "Flygarvägen 217, Järfälla, Sweden";

		GeoLocationParser geoLocationParser = new GeoLocationParser();
		Location actualLocation = geoLocationParser
				.getGeoLocation(adressToSearch);

		assertReflectionEquals(expectedLocation, actualLocation);
	}


}
