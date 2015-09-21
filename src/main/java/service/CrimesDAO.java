package service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;

import model.Crime;
import model.Crimecategory;
import model.Location;

import org.springframework.stereotype.Component;

@Component
public class CrimesDAO {

	private static final String PERSISTENCE_UNIT_NAME = "Crimes";
	private EntityManagerFactory factory;
	private EntityManager em;
	private PersistenceUnitUtil util;

	public CrimesDAO() {

	}

	// TODO: titta p� hur man kan g�ra detta deklarativt med spring
	public void openConnection() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
		util = factory.getPersistenceUnitUtil();
		em = factory.createEntityManager();
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
		openConnection();
		Query crimesQuery = em.createQuery("select c from Crime c");

		// TODO: Hur g�r man korrekt konvertering h�r f�r att s�kra typen?
		List<Crime> resultList = crimesQuery.getResultList();
		closeConnection();
		return resultList;

	}

	public Crime getLatestCrime() {
		openConnection();
		Query crimesQuery = em.createQuery("select c from Crime c order by c.title");
		
		List<Crime> resultList = crimesQuery.getResultList();
		closeConnection();
		if (resultList.isEmpty()) {
			return new Crime();
		}
		return resultList.get(resultList.size()-1);
	}
	
	public List<Crimecategory> getCrimeCategorys(){
		openConnection();
		Query crimesQuery = em.createQuery("select c from Crimecategory c");

		List<Crimecategory> crimecat=crimesQuery.getResultList();
		
		closeConnection();
		return crimecat;
	}


}
