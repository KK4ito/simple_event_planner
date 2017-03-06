package ch.fhnw.edu.eaf.moviemgmt.web;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ch.fhnw.edu.eaf.moviemgmt.domain.Movie;
import ch.fhnw.edu.eaf.moviemgmt.domain.PriceCategory;
import ch.fhnw.edu.eaf.moviemgmt.persistence.MovieRepository;
import ch.fhnw.edu.eaf.moviemgmt.persistence.PriceCategoryRepository;

@RestController
@RequestMapping("/movies")
public class MovieController {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MovieRepository movieRepository;
	
	@Autowired
	private PriceCategoryRepository priceCategoryRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Movie>> findAll() {
		Sort sort = new Sort(Direction.ASC, "id");
		List<Movie> movies = movieRepository.findAll(sort);
		log.debug("Found " + movies.size() + " movies");
		return new ResponseEntity<List<Movie>>(movies, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public ResponseEntity<List<PriceCategory>> findAllPriceCategories() {
		Sort sort = new Sort(Direction.ASC, "id");
		List<PriceCategory> categories = priceCategoryRepository.findAll(sort);
		log.debug("Found " + categories.size() + " price categories");
		return new ResponseEntity<List<PriceCategory>>(categories, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Movie> findById(@PathVariable Long id) {
		Movie movie = movieRepository.findOne(id);
		if (movie == null) {
			return new ResponseEntity<Movie>(HttpStatus.NOT_FOUND);
		}
		log.debug("Found movie with id=" + movie.getId());
		return new ResponseEntity<Movie>(movie, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Movie> create(@Valid @RequestBody Movie movie) {
		movie = movieRepository.save(movie);
		log.debug("Created movie with id=" + movie.getId());
		return new ResponseEntity<Movie>(movie, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Movie> update(@Valid @RequestBody Movie newMovie, @PathVariable Long id) {
		Movie movie = movieRepository.findOne(id);
		if (movie == null) {
			return new ResponseEntity<Movie>(HttpStatus.NOT_FOUND);
		}
		movie.setRented(newMovie.isRented());
		movie.setReleaseDate(newMovie.getReleaseDate());
		movie.setTitle(newMovie.getTitle());
		movieRepository.save(movie);
		log.debug("Updated movie with id=" + movie.getId());
		return new ResponseEntity<Movie>(movie, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id) {
		movieRepository.delete(id);
		log.debug("Deleted movie with id=" + id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
