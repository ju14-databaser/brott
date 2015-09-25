package model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the CRIMECATEGORY database table.
 * @author Erik
 */
@Entity
@NamedQuery(name="Crimecategory.findAll", query="SELECT c FROM Crimecategory c")
@Table(name="CRIMECATEGORY", schema="STOCKHOLM")
public class Crimecategory implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * The id of the category
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	/**
	 * The category
	 */
	private String category;

	/**
	 * A string which contains 'Y' or 'N' to specify if the crime is relevant to a potential house-buyer.
	 */
	private String isrelevant;

	/**
	 * The list of crimes which are mapped to this category using @OneToMany.
	 */
	@OneToMany(mappedBy="crimecategory")
	private List<Crime> crimes;

	/**
	 * Constructor needed for JPA.
	 */
	public Crimecategory() {
	}

	public Crimecategory(String category,String isRelevant){
		this.category = category;
		isrelevant = isRelevant;
	}
	
	/**
	 * 
	 * @return The ID of the category.
	 */
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The category value / name.
	 */
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * 
	 * @return The flag for relevance.
	 */
	public String getIsrelevant() {
		return this.isrelevant;
	}

	public void setIsrelevant(String isrelevant) {
		this.isrelevant = isrelevant;
	}

	/**
	 * 
	 * @return The list of crimes mapped to this category.
	 */
	public List<Crime> getCrimes() {
		return this.crimes;
	}

	public void setCrimes(List<Crime> crimes) {
		this.crimes = crimes;
	}

	/**
	 * Generated by JPA. Maps a crime-object to this category and vice versa.
	 * @param crime The crime to be added.
	 * @return The crime. 
	 */
	public Crime addCrime(Crime crime) {
		getCrimes().add(crime);
		crime.setCrimecategory(this);

		return crime;
	}

	/**
	 * Generated by JPA. Removes the mapped crime object from this category.
	 * @param crime the crime to be removed.
	 * @return The crime.
	 */
	public Crime removeCrime(Crime crime) {
		getCrimes().remove(crime);
		crime.setCrimecategory(null);

		return crime;
	}

}