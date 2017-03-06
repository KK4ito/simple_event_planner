package ch.fhnw.edu.eaf.moviemgmt.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "movies")
public class Movie {
	@Id
	@GeneratedValue
	@Column(name = "movie_id")
	private Long id;

	@Column(name = "movie_title")
	private String title;

	@Column(name = "movie_rented")
	private boolean rented;

	@Column(name = "movie_releasedate")
	private Date releaseDate;

	@ManyToOne
	@JoinColumn(name = "pricecategory_fk")
	private PriceCategory priceCategory;

	protected Movie() {
	}

	public Movie(String title, Date releaseDate, PriceCategory priceCategory)
			throws NullPointerException {
		if ((title == null) || (releaseDate == null)
				|| (priceCategory == null)) {
			throw new NullPointerException("not all input parameters are set!");
		}
		this.title = title;
		this.releaseDate = releaseDate;
		this.priceCategory = priceCategory;
		this.rented = false;
	}

	public PriceCategory getPriceCategory() {
		return priceCategory;
	}

	public void setPriceCategory(PriceCategory priceCategory) {
		this.priceCategory = priceCategory;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}
	
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public boolean isRented() {
		return rented;
	}

	public void setRented(boolean rented) {
		this.rented = rented;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
