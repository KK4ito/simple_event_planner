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

import ch.fhnw.edu.eaf.eventmgmt.domain.Event;
import ch.fhnw.edu.eaf.eventmgmt.persistence.EventRepository;

//@RestController
//@RequestMapping("/events")
public class EventController {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private EventRepository eventRepository;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Event>> findAll() {
		Sort sort = new Sort(Direction.ASC, "id");
		List<Event> events = eventRepository.findAll(sort);
		log.debug("Found " + events.size() + " events");
		return new ResponseEntity<List<Event>>(events, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Event> findById(@PathVariable Long id) {
		Event event = eventRepository.findOne(id);
		if (event == null) {
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
		}
		log.debug("Found event with id=" + event.getId());
		return new ResponseEntity<Event>(event, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Event> create(@Valid @RequestBody Event event) {
		event = eventRepository.save(event);
		log.debug("Created event with id=" + event.getId());
		return new ResponseEntity<Event>(event, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Event> update(@Valid @RequestBody Event newEvent, @PathVariable Long id) {
		Event event = eventRepository.findOne(id);
		if (event == null) {
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
		}
		event.setTitle(newEvent.getTitle());
		event.setDescription(newEvent.getDescription());
		event.setStartTime(newEvent.getStartTime());
		event.setClosingTime(newEvent.getClosingTime());
		event.setEndTime(newEvent.getEndTime());
		event.setFiles(newEvent.getFiles());

		eventRepository.save(event);
		log.debug("Updated event with id=" + event.getId());
		return new ResponseEntity<Event>(event, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> delete(@PathVariable Long id) {
		eventRepository.delete(id);
		log.debug("Deleted event with id=" + id);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
