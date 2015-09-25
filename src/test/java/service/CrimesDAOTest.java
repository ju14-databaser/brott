package service;

import java.util.List;

import model.Crime;
import model.Crimecategory;
import model.Location;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unitils.reflectionassert.ReflectionAssert;

public class CrimesDAOTest {

	private CrimesDAO crimesDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(CrimesDAOTest.class);
	private Crimecategory cat;
	private static final String TITLE = "2015-09-13 15:53, Skadegörelse, Botkyrka";
	private static final String DESC = "Sedelvägen, Tumba. Barn krossat rutor på förskola.";
	private static final String crimeTitle1 = "2015-09-22 06:59, Stöld, försök, Järfälla";
	private static final String crimeDesc1 = "Tomasvägen, Jakobsberg. Ynglingar som bryter på mopeder.";
	private static final String crimeTitle2 = "2015-09-22 02:29, Skadegörelse, Stockholm";
	private static final String crimeDesc2 = "Tunnelbanestation Högdalen. Klottrare som somnat efter sitt klottrande.";
	private Crime simpleCrime;
	private Crime crime1;
	private Crime crime2;
	private Crimecategory cat2;

	@Before
	public void setup() {
		simpleCrime = new Crime(TITLE, DESC);
		crime1 = new Crime(crimeTitle1, crimeDesc1);
		crime2 = new Crime(crimeTitle2, crimeDesc2);

		LOGGER.info("Starting in-memory database for unit tests");
		cat = new Crimecategory();
		cat.setCategory("skadegörelse");
		cat.setIsrelevant("y");
		cat2 = new Crimecategory();
		cat2.setCategory("bråk");
		cat2.setIsrelevant("y");

		// Creating testDB database

		crimesDAO = new CrimesDAO("TestDB");

		crimesDAO.addCrimecategory(cat);
	}

	@Test
	public void insertCrime_RetrieveCrime_FieldsAreEqual() {

		addSimpleCrimeToTestDB();

		List<Crime> allCrimes = crimesDAO.getAllCrimes();
		Crime actualCrime = null;
		for (Crime crime : allCrimes) {
			if (crime.getTitle().equals(simpleCrime.getTitle())) {
				actualCrime = crime;
			}
		}

		Assert.assertEquals(simpleCrime.getTitle(), actualCrime.getTitle());
		Assert.assertEquals(simpleCrime.getDescription(), actualCrime.getDescription());
		Assert.assertEquals(simpleCrime.getDateStamp(), actualCrime.getDateStamp());
		Assert.assertEquals(simpleCrime.getLocation(), actualCrime.getLocation());

	}

	private void addSimpleCrimeToTestDB() {
		crimesDAO.openConnection();
		crimesDAO.addCrime(simpleCrime);
		crimesDAO.closeConnection();
	}

	/**
	 * Ignore on this test since the database is currently not cleaned between
	 * each test - so this test is not always working.
	 */
	@Ignore
	@Test
	public void twoCrimesInDatabase_retriveLatestCrime() {
		crimesDAO.openConnection();
		crimesDAO.addCrime(crime1);
		crimesDAO.addCrime(crime2);
		crimesDAO.closeConnection();

		Assert.assertEquals(crime1.getTitle(), crimesDAO.getLatestCrime().getTitle());
	}

	@Test
	public void insertCrime_GetCrimeBack() {
		addSimpleCrimeToTestDB();

		Crime actualCrime = crimesDAO.getCrime(simpleCrime);

		Assert.assertEquals(simpleCrime.getTitle(), actualCrime.getTitle());
		Assert.assertEquals(simpleCrime.getDescription(), actualCrime.getDescription());
		Assert.assertEquals(simpleCrime.getDateStamp(), actualCrime.getDateStamp());
		Assert.assertEquals(simpleCrime.getLocation(), actualCrime.getLocation());

	}

	@Test
	public void updateLocationOnCrime_UpdateDatasource_GetUpdatedObjectBack_LocationIsCorrect() {
		addSimpleCrimeToTestDB();
		Location expectedLocation = new Location("59.39845440000001", "17.8707829");
		
		Crime expectedCrime = crimesDAO.getCrime(simpleCrime);
		Assert.assertNull(expectedCrime.getGeoLocation());

		crimesDAO.openConnection();
		crimesDAO.updateCrimeGeoLocation(simpleCrime, expectedLocation);
		crimesDAO.closeConnection();
		Crime actualCrime = crimesDAO.getCrime(simpleCrime);

		ReflectionAssert.assertReflectionEquals(expectedLocation, actualCrime.getGeoLocation());
	}

	@Test
	public void insertTwoCrimeCategories_retrieveSameTwoCrimeCategories() {
		crimesDAO.addCrimecategory(cat2);
		Crimecategory fightcat = crimesDAO.getCrimeCategory(cat2);
		Crimecategory otherCat = crimesDAO.getCrimeCategory(cat);

		ReflectionAssert.assertReflectionEquals(cat, otherCat);
		ReflectionAssert.assertReflectionEquals(cat2, fightcat);
	}

	@Test
	public void insertCrimeCategory_getSameCrimeCategoryBack() {

		crimesDAO.addCrimecategory(cat2);

		Crimecategory crimeCategory = crimesDAO.getCrimeCategory(cat2);

		ReflectionAssert.assertReflectionEquals(cat2, crimeCategory);
	}

	@Test
	public void getCrimeCategoryFromDatasourceBasedOnString() {

		crimesDAO.addCrimecategory(cat2);

		Crimecategory singleResult = crimesDAO.getCrimecategory("bråk");

		ReflectionAssert.assertReflectionEquals(cat2, singleResult);

	}

}
