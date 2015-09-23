package service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Crime;
import model.Crimecategory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unitils.reflectionassert.ReflectionAssert;

public class CrimesDAOTest {

	private EntityManager em;
	private EntityManagerFactory factory;
	private CrimesDAO crimesDAO;
	private static final Logger LOGGER = LoggerFactory.getLogger(CrimesDAOTest.class);
	private Crimecategory cat;
	private static final String TITLE = "2015-09-13 15:53, Skadeg�relse, Botkyrka";
	private static final String DESC = "Sedelv�gen, Tumba. Barn krossat rutor p� f�rskola.";
	private static final String crimeTitle1 = "2015-09-22 06:59, St�ld, f�rs�k, J�rf�lla";
	private static final String crimeDesc1 = "Tomasv�gen, Jakobsberg. Ynglingar som bryter p� mopeder.";
	private static final String crimeTitle2 = "2015-09-22 02:29, Skadeg�relse, Stockholm";
	private static final String crimeDesc2 = "Tunnelbanestation H�gdalen. Klottrare som somnat efter sitt klottrande.";
	private Crime simpleCrime;
	private Crime crime1;
	private Crime crime2;

	@Before
	public void setup() {
		simpleCrime = new Crime(TITLE, DESC);
		crime1 = new Crime(crimeTitle1, crimeDesc1);
		crime2 = new Crime(crimeTitle2, crimeDesc2);
		LOGGER.info("Starting in-memory database for unit tests");

		// Creating testDB database

		crimesDAO = new CrimesDAO("TestDB");
		factory = Persistence.createEntityManagerFactory("TestDB");

		em = factory.createEntityManager();
		cat = new Crimecategory();
		cat.setCategory("skadeg�relse");
		cat.setIsrelevant("y");
		em.getTransaction().begin();
		em.persist(cat);
		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void insertCrime_RetrieveCrime_FieldsAreEqual() {

		crimesDAO.openConnection();
		crimesDAO.addCrime(simpleCrime);
		crimesDAO.closeConnection();

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

	@Test
	public void twoCrimesInDatabase_retriveLatestCrime() {
		crimesDAO.openConnection();					
		crimesDAO.addCrime(crime1);
		crimesDAO.addCrime(crime2);
		crimesDAO.closeConnection();

		Assert.assertEquals(crime1.getTitle(), crimesDAO.getLatestCrime().getTitle());
	}
	
	@Test
	public void updateCrimetest(){
		//TODO: skriv testet
	}
	
		
}
