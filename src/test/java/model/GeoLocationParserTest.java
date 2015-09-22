package model;

import org.junit.Test;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class GeoLocationParserTest {

	// TODO: test som kollar att en location får rätt koordinater.

	@Test
	public void adressSentInForParsing_correctLocationIsCreated() {
		
		Location expectedLocation = new Location("59.39845440000001", "17.8707829");
		String adressToSearch = "Flygarvägen 217, Järfälla, Sweden";

		GeoLocationParser geoLocationParser = new GeoLocationParser();
		Location actualLocation = geoLocationParser.getGeoLocation(adressToSearch);

		assertReflectionEquals(expectedLocation, actualLocation);
	}

	// TODO: test som fångar rätt exceptions vid olika fel

}
