package br.hikarikun92.restdemo.person;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return repository.getAll();
    }

    @GetMapping(value = "{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Person> getByName(@PathVariable String name) {
        Optional<Person> optional = repository.getByName(name);

        if (optional.isPresent()) {
            //If there was a person with that name, return a response code of 200 (OK) and the person data converted to JSON
            return ResponseEntity.ok(optional.get());
        } else {
            //Otherwise, return a status code of 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }
}
