package ch.fhnw.edu.eaf.moviemgmt.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@Table(name = "pricecategories")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("PriceCategory")
@DiscriminatorColumn(name = "pricecategory_type")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @Type(value = PriceCategoryChildren.class, name = "Children"),
    @Type(value = PriceCategoryRegular.class, name = "Regular"),
    @Type(value = PriceCategoryNewRelease.class, name = "NewRelease") }
)
public abstract class PriceCategory {
	@Id
	@GeneratedValue
	@Column(name = "pricecategory_id")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public abstract double getCharge(int daysRented);

	public abstract String getName();

	public int getFrequentRenterPoints(int daysRented) {
		return 1;
	}
}
