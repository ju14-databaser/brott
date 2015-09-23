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

@Component
public class CrimesDAO {

	private static final String PERSISTENCE_UNIT_NAME = "Crimes";
	private EntityManagerFactory factory;
	private EntityManager em;
	private PersistenceUnitUtil util;
	private static final Logger LOGGER = LoggerFactory.getLogger(CrimesDAO.class);
	private String persistenceUnitName;

	public CrimesDAO() {
		persistenceUnitName = PERSISTENCE_UNIT_NAME;
	}

	public CrimesDAO(String persistenceUnitName) {
		this.persistenceUnitName = persistenceUnitName;

	}

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

	public void closeConnection() {
		em.close();
	}

	public void addCrime(Crime crime) {

		em.getTransaction().begin();
		em.persist(crime);
		em.getTransaction().commit();

	}

	public void updateCrime(Crime crime, Location geoLocation) {

		Object identifier = util.getIdentifier(crime);

		Crime crimeQuery = em.find(Crime.class, identifier);
		crimeQuery.setGeoLocation(geoLocation);

		em.getTransaction().begin();
		em.merge(crimeQuery);
		em.getTransaction().commit();

	}

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

	// TODO: egen klass?
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
