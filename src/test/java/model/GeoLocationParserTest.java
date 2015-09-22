package model;

import org.junit.Test;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class GeoLocationParserTest {

	// TODO: test som kollar att en location f�r r�tt koordinater.

	@Test
	public void adressSentInForParsing_correctLocationIsCreated() {
		
		Location expectedLocation = new Location("59.39845440000001", "17.8707829");
		String adressToSearch = "Flygarv�gen 217, J�rf�lla, Sweden";

		GeoLocationParser geoLocationParser = new GeoLocationParser();
		Location actualLocation = geoLocationParser.getGeoLocation(adressToSearch);

		assertReflectionEquals(expectedLocation, actualLocation);
	}

	// TODO: test som f�ngar r�tt exceptions vid olika fel

}
