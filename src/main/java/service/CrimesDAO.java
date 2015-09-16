package service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import model.Crime;

import org.springframework.stereotype.Component;

@Component
public class CrimesDAO {

	private static final String PERSISTENCE_UNIT_NAME = "Crimes";
	private EntityManagerFactory factory;
	private EntityManager em;

	public CrimesDAO() {

	}

	// TODO: titta på hur man kan göra detta deklarativt med spring
	public void openConnection() {
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
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

	public List<Crime> getAllCrimes() {

		Query crimesQuery = em.createQuery("select c from Crime c");
		
		// TODO: Hur gör man korrekt kovertering här för att säkra typen?
		List<Crime> resultList = crimesQuery.getResultList();
		
		return resultList;

	}

}
