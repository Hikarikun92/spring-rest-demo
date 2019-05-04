package br.hikarikun92.restdemo.person;

import org.springframework.http.HttpStatus;
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

    //As we will not return anything, a status of "No Content" is appropriate
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void update(@PathVariable int id, @RequestBody Person person) {
        //Set the ID manually to avoid a possibly wrong payload
        person.setId(id);

        //Observe that, when saving the person like this, it will update an existing entity correctly, but if the person
        //with the requested ID does not exist, it will be created with even another ID. How to fix that?
        repository.save(person);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "{id}")
    public void delete(@PathVariable int id) {
        //This method will delete the person if it exists and ignore it if it doesn't. Maybe it would be good to return
        //HTTP 404 if it doesn't exist, but that is up to you
        final Optional<Person> optional = repository.findById(id);
        optional.ifPresent(repository::delete);
    }
}
