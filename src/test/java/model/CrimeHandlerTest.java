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
	
	@Test
	public void checkIfRightCategoryFound() throws Exception {

		CrimesDAO crimesDAO = new CrimesDAO("Crimes");
		CrimeHandler crimeHandler = new CrimeHandler(crimesDAO);

		List<Crimecategory> crimecat = crimeHandler.getAllCategorysFromDB();
		List<Crime> crimeList= new ArrayList<Crime>();
		crimeList.add(new Crime("2015-09-22 12:14, Brand, Huddinge","Holmträskvägen, Sundby. Brand i lagerbyggnad."));
		crimeList.add(new Crime("2015-09-22 06:59, Stöld, försök, Järfälla","Tomasvägen, Jakobsberg. Ynglingar som bryter på mopeder."));	
		crimeList.add(new Crime("2015-09-22 06:11, Trafikolycka, personskada, Solna","E4, Järva Krog, Ulriksdal. Två personbilar i sammanstötning."));
		crimeList.add(new Crime("2015-09-21 07:27, Trafikolycka, personskada, Norrtälje","Norrtälje. Cyklist påkörd."));
		crimeList.add(new Crime("2015-09-21 01:37, Våld/hot mot tjänsteman, Upplands Väsby","Centrumanläggning Bergkällavägen."));
		crimeList.add(new Crime("2015-09-18 09:38, Djur skadat/omhändertaget, Huddinge","Sundby. En anmälare tror en katt är självmordsbenägen."));
		
		crimeList=crimeHandler.setCrimeCategory(crimeList, crimecat);
		
		assertReflectionEquals(" Brand".toLowerCase(), crimeList.get(0).getCategory().toLowerCase());
		assertReflectionEquals(" Stöld".toLowerCase(), crimeList.get(1).getCategory().toLowerCase());
		assertReflectionEquals(" Trafikolycka".toLowerCase(), crimeList.get(2).getCategory().toLowerCase());
		assertReflectionEquals(" Trafikolycka".toLowerCase(), crimeList.get(3).getCategory().toLowerCase());
		assertReflectionEquals(" Våld/hot mot tjänsteman".toLowerCase(), crimeList.get(4).getCategory().toLowerCase());
		assertReflectionEquals(" Djur skadat/omhändertaget".toLowerCase(), crimeList.get(5).getCategory().toLowerCase());
	}

}
