package model;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import org.junit.Ignore;
import org.junit.Test;

public class GeoLocationParserTest {

	/**
	 * This test will only work when there is an internet connection. It is
	 * supposed to be run manually.
	 * 
	 * @author Lina
	 * 
	 */
	@Ignore
	@Test
	public void adressSentInForParsing_correctLocationIsCreated() {

		Location expectedLocation = new Location("59.39845440000001", "17.8707829");
		String adressToSearch = "Flygarvägen 217, Järfälla, Sweden";

		GeoLocationParser geoLocationParser = new GeoLocationParser();
		Location actualLocation = geoLocationParser.getGeoLocation(adressToSearch);

		assertReflectionEquals(expectedLocation, actualLocation);
	}

}
