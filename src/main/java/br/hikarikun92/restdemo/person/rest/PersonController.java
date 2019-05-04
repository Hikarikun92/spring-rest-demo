package br.hikarikun92.restdemo.person.rest;

import br.hikarikun92.restdemo.person.rest.dto.PersonReadDto;
import br.hikarikun92.restdemo.person.rest.dto.PersonWriteDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("people")
public class PersonController {
    private final PersonRestFacade facade;

    public PersonController(PersonRestFacade facade) {
        this.facade = facade;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PersonReadDto> getAll() {
        //Retrieve all the people present in the application, converted to a JSON list
        return facade.getAll();
    }

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PersonReadDto> getById(@PathVariable int id) {
        Optional<PersonReadDto> optional = facade.getById(id);

        if (optional.isPresent()) {
            //If there was a person with that name, return a response code of 200 (OK) and the person data converted to JSON
            return ResponseEntity.ok(optional.get());
        } else {
            //Otherwise, return a status code of 404 (Not Found)
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/name/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PersonReadDto> searchByName(@PathVariable String name) {
        return facade.searchByName(name);
    }

    @GetMapping(value = "/custom", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<PersonReadDto> customSearch(@RequestParam String name, @RequestParam int age) {
        return facade.customSearch(name, age);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> create(@RequestBody @Valid PersonWriteDto dto) {
        final int id = facade.create(dto);
        return ResponseEntity.created(URI.create("/" + id)).build();
    }

    @PutMapping(value = "{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody @Valid PersonWriteDto dto) {
        final boolean found = facade.update(id, dto);
        if (found) {
            //If the person was found and updated, return "No Content"
            return ResponseEntity.noContent().build();
        } else {
            //If not, return HTTP 404
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        final boolean found = facade.delete(id);
        if (found) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
