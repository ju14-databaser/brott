package model;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import service.CrimesDAO;

public class CrimeHandlerTest {

	/**
	 * 
	 * Annotation Ignore due to that an internetconnection is needed for this test to run.
	 * @author Lina
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void updateGeoLocationsStopsWhenReachingEndOfDatabase() throws Exception {
		CrimesDAO crimesDAO = new CrimesDAO("TestDB");
		CrimeFacade crimeHandler = new CrimeFacade(crimesDAO);

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

	/**
	 * 
	 * @author Erik
	 * @throws Exception
	 */
	@Test
	public void checkIfRightCategoryFound() throws Exception {

		CrimesDAO crimesDAO = new CrimesDAO("Crimes");
		CrimeFacade crimeHandler = new CrimeFacade(crimesDAO);

		List<Crimecategory> crimecat = crimeHandler.getAllCategorysFromDB();
		List<Crime> crimeList = new ArrayList<Crime>();
		crimeList.add(new Crime("2015-09-22 12:14, Brand, Huddinge",
				"Holmträskvägen, Sundby. Brand i lagerbyggnad."));
		crimeList.add(new Crime("2015-09-22 06:59, Stöld, försök, Järfälla",
				"Tomasvägen, Jakobsberg. Ynglingar som bryter på mopeder."));
		crimeList.add(new Crime("2015-09-22 06:11, Trafikolycka, personskada, Solna",
				"E4, Järva Krog, Ulriksdal. Två personbilar i sammanstötning."));
		crimeList.add(new Crime("2015-09-21 07:27, Trafikolycka, personskada, Norrtälje",
				"Norrtälje. Cyklist påkörd."));
		crimeList.add(new Crime("2015-09-21 01:37, Våld/hot mot tjänsteman, Upplands Väsby",
				"Centrumanläggning Bergkällavägen."));
		crimeList.add(new Crime("2015-09-18 09:38, Djur skadat/omhändertaget, Huddinge",
				"Sundby. En anmälare tror en katt är självmordsbenägen."));

		crimeList = crimeHandler.setCrimeCategory(crimeList, crimecat);

		assertReflectionEquals("Brand", crimeList.get(0).getCrimecategory().getCategory());
		assertReflectionEquals("Stöld", crimeList.get(1).getCrimecategory().getCategory());
		assertReflectionEquals("Trafikolycka", crimeList.get(2).getCrimecategory().getCategory());
		assertReflectionEquals("Trafikolycka", crimeList.get(3).getCrimecategory().getCategory());
		assertReflectionEquals("Våld", crimeList.get(4).getCrimecategory().getCategory());
		assertReflectionEquals("Djur skadat/omhändertaget", crimeList.get(5).getCrimecategory()
				.getCategory());
	}

}
