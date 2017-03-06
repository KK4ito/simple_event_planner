package ch.fhnw.edu.eaf.rentalmgmt.web.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect
public class MovieDTO {
	@JsonProperty("id")
	private Long id;

	@JsonProperty("title")
	private String title;

	@JsonProperty("rented")
	private boolean rented;

	@JsonProperty("releaseDate")
	private Date releaseDate;

	@JsonProperty("priceCategory")
	private PriceCategoryDTO priceCategory;
}

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
class PriceCategoryDTO {
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("name")
	private String name;
}
