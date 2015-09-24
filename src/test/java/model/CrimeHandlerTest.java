package model;

import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import commonj.sdo.helper.XMLHelper;
import service.CrimesDAO;

public class CrimeHandlerTest {

	// TODO: Kör ett integrationstest - läs in en xml fil via parser och
	// kontrollera att innehållet hamnar i en databas (helst en embedded db
	// enbart för test)

	// TODO: test på vilka kategorier som ett Crime får

	@Test
	public void updateGeoLocationsStopsWhenReachingEndOfDatabase() throws Exception {
		CrimesDAO crimesDAO = new CrimesDAO("TestDB");
		CrimeHandler crimeHandler = new CrimeHandler(crimesDAO);

		String correctRssFeed = "src/test/resources/CrimesRSSForDBTests.xml";
		XMLParser xmlParser = new XMLParser(correctRssFeed);

		List<Crime> parseAllCrimes = xmlParser.parseAllCrimes();
		
		crimesDAO.openConnection();
		for (Crime crime : parseAllCrimes) {
			crimesDAO.addCrime(crime);
		}
		crimesDAO.closeConnection();

		crimeHandler.updateGeoLocationsScheduled();
		while (!crimeHandler.isAllHasGeoLocation()) {
			crimeHandler.updateGeoLocations();
		}

		Assert.assertTrue(crimeHandler.updateGeoLocations() == 0);
		Assert.assertTrue(crimeHandler.isAllHasGeoLocation());

	}

}
