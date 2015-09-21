package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CRIMECATEGORY database table.
 * 
 */
@Entity
@NamedQuery(name="Crimecategory.findAll", query="SELECT c FROM Crimecategory c")
@Table(name="CRIMECATEGORY", schema="STOCKHOLM")
public class Crimecategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	private String category;

	private String isrelevant;

	//bi-directional many-to-one association to Crimetest
	@OneToMany(mappedBy="crimecategory")
	private List<Crime> crimes;

	public Crimecategory() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getIsrelevant() {
		return this.isrelevant;
	}

	public void setIsrelevant(String isrelevant) {
		this.isrelevant = isrelevant;
	}

	public List<Crime> getCrimes() {
		return this.crimes;
	}

	public void setCrimes(List<Crime> crimes) {
		this.crimes = crimes;
	}

	public Crime addCrime(Crime crime) {
		getCrimes().add(crime);
		crime.setCrimecategory(this);

		return crime;
	}

	public Crime removeCrime(Crime crime) {
		getCrimes().remove(crime);
		crime.setCrimecategory(null);

		return crime;
	}

}