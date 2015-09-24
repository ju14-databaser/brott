package service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.TypedQuery;

import model.Crime;
import model.Crimecategory;
import model.Location;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * CrimesDAO connects to a database specified in the persistence.xml file.
 *
 */

@Component
public class CrimesDAO {

	private static final String PERSISTENCE_UNIT_NAME = "Crimes";
	private EntityManagerFactory factory;
	private EntityManager em;
	private PersistenceUnitUtil util;
	private static final Logger LOGGER = LoggerFactory.getLogger(CrimesDAO.class);
	private String persistenceUnitName;

	/**
	 * Default constructor that uses the Crimes database for production.
	 */
	public CrimesDAO() {
		persistenceUnitName = PERSISTENCE_UNIT_NAME;
	}

	/**
	 * Constructor that allows the user to send in the name of the datasource
	 * needed. The datasource nmae should already be specified in a
	 * persistence.xml file.
	 * 
	 * @param persistenceUnitName
	 */
	public CrimesDAO(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;
	}

	/**
	 * openConnection creates the entityManager to enable transactions to the
	 * datasource. When the transaction is done it should be followed by the
	 * closeConnection method that closes the entityManager.
	 * 
	 * @throws PersistenceException
	 *             when there are troubles connecting to the datasource.
	 */
	public void openConnection() throws PersistenceException {
		LOGGER.debug("starting to open connection to DB");
		try {
			factory = Persistence.createEntityManagerFactory(persistenceUnitName);
			util = factory.getPersistenceUnitUtil();
			em = factory.createEntityManager();

		} catch (PersistenceException e) {
			LOGGER.error("Error when connecting to the database. " + e.getMessage());
			throw e;
		}

		LOGGER.debug("Succesfully opened connection to DB");
	}

	/**
	 * closeConnection closes an open EntityManager. Should always be used after
	 * a transaction.
	 */
	public void closeConnection() {
		em.close();
	}

	/**
	 * Adds a new crime to the datasource
	 * 
	 * @param crime
	 */
	public void addCrime(Crime crime) {

		em.getTransaction().begin();
		em.persist(crime);
		em.getTransaction().commit();

	}

	/**
	 * Adds a new crimecategory to the datasource.
	 * 
	 * Rememeber to openConnection before and closeConnection after.
	 * 
	 * @param crimecategory
	 */
	public void addCrimecategory(Crimecategory crimecategory) {
		em.getTransaction().begin();
		em.persist(crimecategory);
		em.getTransaction().commit();
	}

	/**
	 * updateCrime takes the parameter Crime (that should already exist in the
	 * datasource) and updates it in the datasource
	 * 
	 * Rememeber to openConnection before and closeConnection after.
	 * 
	 * @param crime
	 * @param geoLocation
	 */
	public void updateCrime(Crime crime, Location geoLocation) {
		// TODO: Check that this works as expected, if the location can be added
		// before updating
		Object identifier = util.getIdentifier(crime);

		Crime crimeQuery = em.find(Crime.class, identifier);
		crimeQuery.setGeoLocation(geoLocation);

		em.getTransaction().begin();
		em.merge(crimeQuery);
		em.getTransaction().commit();

	}

	/**
	 * Retrieves all Crimes from the datasource. The method takes care of both
	 * opening and closing the connection.
	 * 
	 * @return a List of all Crimes existing in the datasource.
	 * @throws PersistenceException
	 *             if there are troubles when connecting to the datasource
	 */
	public List<Crime> getAllCrimes() {
		try {
			openConnection();
		} catch (PersistenceException e) {
			LOGGER.error("Error when connecting to the database. " + e.getMessage());
			throw e;
		}
		TypedQuery<Crime> crimesQuery = em.createQuery("select c from Crime c", Crime.class);

		List<Crime> resultList = crimesQuery.getResultList();
		closeConnection();
		return resultList;

	}

	/**
	 * getLatestCrime gets the Crime with the newest Title String from the
	 * datasource.
	 * 
	 * The method takes care of both opening and closing the connection.
	 * 
	 * @throws PersistenceException
	 *             when there are troubles connecting to the datasource
	 * @return Crime with the newest datestamp in the Title String
	 * 
	 * @return a new empty Crime if there are no Crimes in the datasource
	 */
	public Crime getLatestCrime() {
		try {
			openConnection();
		} catch (PersistenceException e) {
			LOGGER.error("Error when connecting to the database. " + e.getMessage());
			throw e;
		}
		TypedQuery<Crime> crimesQuery = em.createQuery("select c from Crime c order by c.title",
				Crime.class);

		List<Crime> resultList = crimesQuery.getResultList();
		closeConnection();
		if (resultList.isEmpty()) {
			return new Crime();
		}
		return resultList.get(resultList.size() - 1);
	}

	/**
	 * Retrieves all CrimeCategories from the datasource.
	 *
	 * The method takes care of both opening and closing the connection.
	 * 
	 * @return a list of all CrimeCategories
	 * @throws PersistenceException
	 *             if there are no connection to the datasource
	 * 
	 */
	public List<Crimecategory> getCrimeCategorys() {
		try {
			openConnection();
		} catch (PersistenceException e) {
			LOGGER.error("Error when connecting to the database. " + e.getMessage());
			throw e;
		}
		TypedQuery<Crimecategory> crimesQuery = em.createQuery("select c from Crimecategory c",
				Crimecategory.class);

		List<Crimecategory> crimecat = crimesQuery.getResultList();

		closeConnection();
		return crimecat;
	}

}
