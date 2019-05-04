package br.hikarikun92.restdemo.person;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("people")
public class PersonController {

    private final PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Person> getAll() {
        //Retrieve all the people present in the application, converted to a JSON list
        return repository.findAll();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Person> getById(@PathVariable int id) {
        Optional<Person> optional = repository.findById(id);

        if (optional.isPresent()) {
            //If there was a person with that name, return a response code of 200 (OK) and the person data converted to JSON
            return ResponseEntity.ok(optional.get());
        } else {
            //Otherwise, return a status code of 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Person> searchByName(@PathVariable String name) {
        return repository.findAllByNameContaining(name);
    }

    @GetMapping(value = "/custom", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<Person> customSearch(@RequestParam String name, @RequestParam int age) {
        return repository.findUsingMyCustomRule(name, age);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> create(@RequestBody Person person) {
        //If the ID is set, an existing entity in the database will be updated instead, which is incorrect
        person.setId(null);

        final Person savedPerson = repository.save(person);
        return ResponseEntity.created(URI.create("/" + savedPerson.getId())).build();
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Person person) {
        //Set the ID manually to avoid a possibly wrong payload
        person.setId(id);

        final Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) {
            //If the person exists, update it and return "No Content"
            repository.save(person);

            return ResponseEntity.noContent().build();
        } else {
            //If not, return HTTP 404 and don't try to save it
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        //This method will delete the person if it exists and ignore it if it doesn't. Maybe it would be good to return
        //HTTP 404 if it doesn't exist, but that is up to you
        final Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) {
            repository.delete(optional.get());

            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
