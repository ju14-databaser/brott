package model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import commonj.sdo.helper.XMLHelper;
import service.CrimesDAO;

public class CrimeHandlerTest {

	// TODO: K�r ett integrationstest - l�s in en xml fil via parser och
	// kontrollera att inneh�llet hamnar i en databas (helst en embedded db
	// enbart f�r test)

	// TODO: test p� vilka kategorier som ett Crime f�r

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

	@Test
	public void checkIfRightCategoryFound() throws Exception {

		CrimesDAO crimesDAO = new CrimesDAO("Crimes");
		CrimeHandler crimeHandler = new CrimeHandler(crimesDAO);

		List<Crimecategory> crimecat = crimeHandler.getAllCategorysFromDB();
		List<Crime> crimeList = new ArrayList<Crime>();
		crimeList.add(new Crime("2015-09-22 12:14, Brand, Huddinge",
				"Holmtr�skv�gen, Sundby. Brand i lagerbyggnad."));
		crimeList.add(new Crime("2015-09-22 06:59, St�ld, f�rs�k, J�rf�lla",
				"Tomasv�gen, Jakobsberg. Ynglingar som bryter p� mopeder."));
		crimeList.add(new Crime("2015-09-22 06:11, Trafikolycka, personskada, Solna",
				"E4, J�rva Krog, Ulriksdal. Tv� personbilar i sammanst�tning."));
		crimeList.add(new Crime("2015-09-21 07:27, Trafikolycka, personskada, Norrt�lje",
				"Norrt�lje. Cyklist p�k�rd."));
		crimeList.add(new Crime("2015-09-21 01:37, V�ld/hot mot tj�nsteman, Upplands V�sby",
				"Centrumanl�ggning Bergk�llav�gen."));
		crimeList.add(new Crime("2015-09-18 09:38, Djur skadat/omh�ndertaget, Huddinge",
				"Sundby. En anm�lare tror en katt �r sj�lvmordsben�gen."));

		crimeList = crimeHandler.setCrimeCategory(crimeList, crimecat);

		assertReflectionEquals("Brand", crimeList.get(0).getCrimecategory().getCategory());
		assertReflectionEquals("St�ld", crimeList.get(1).getCrimecategory().getCategory());
		assertReflectionEquals("Trafikolycka", crimeList.get(2).getCrimecategory().getCategory());
		assertReflectionEquals("Trafikolycka", crimeList.get(3).getCrimecategory().getCategory());
		assertReflectionEquals("V�ld", crimeList.get(4).getCrimecategory().getCategory());
		assertReflectionEquals("Djur skadat/omh�ndertaget", crimeList.get(5).getCrimecategory()
				.getCategory());
	}

}
