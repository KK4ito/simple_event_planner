package ch.fhnw.edu.eaf.eventmgmt.web;

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

import ch.fhnw.edu.eaf.eventmgmt.domain.User;
import ch.fhnw.edu.eaf.eventmgmt.persistence.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<User>> findAll() {
		Sort sort = new Sort(Direction.ASC, "id");
		List<User> users = userRepository.findAll(sort);
		log.debug("Found " + users.size() + " users");
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> findById(@PathVariable Long id) {
		User user = userRepository.findOne(id);
		if (user == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		log.debug("Found user with id=" + user.getId());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<User> create(@Valid @RequestBody User user) {
		user = userRepository.save(user);
		log.debug("Created user with id=" + user.getId());
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<User> update(@Valid @RequestBody User newUser, @PathVariable Long id) {
		User user = userRepository.findOne(id);
		if (user == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		user.setFirstName(newUser.getFirstName());
		user.setLastName(newUser.getLastName());
		user.setEmail(newUser.getEmail());
		userRepository.save(user);
		log.debug("Updated user with id=" + user.getId());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id) {
		userRepository.delete(id);
		log.debug("Deleted user with id=" + id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
