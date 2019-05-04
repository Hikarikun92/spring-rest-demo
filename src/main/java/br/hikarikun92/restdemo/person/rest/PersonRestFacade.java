package br.hikarikun92.restdemo.person.rest;

import br.hikarikun92.restdemo.person.Person;
import br.hikarikun92.restdemo.person.PersonRepository;
import br.hikarikun92.restdemo.person.rest.dto.PersonReadDto;
import br.hikarikun92.restdemo.person.rest.dto.PersonWriteDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonRestFacade {
    private final PersonRepository repository;

    public PersonRestFacade(PersonRepository repository) {
        this.repository = repository;
    }

    public List<PersonReadDto> getAll() {
        final List<Person> entities = repository.findAll();
        return toReadDtos(entities);
    }

    private List<PersonReadDto> toReadDtos(List<Person> entities) {
        final List<PersonReadDto> dtos = new ArrayList<>(entities.size());
        for (Person entity : entities) {
            dtos.add(toReadDto(entity));
        }
        return dtos;
    }

    private PersonReadDto toReadDto(Person entity) {
        return new PersonReadDto(entity.getId(), entity.getName(), entity.getAge());
    }

    public Optional<PersonReadDto> getById(int id) {
        final Optional<Person> optional = repository.findById(id);
        return optional.map(this::toReadDto);
    }

    public List<PersonReadDto> searchByName(String name) {
        final List<Person> entities = repository.findAllByNameContaining(name);
        return toReadDtos(entities);
    }

    public List<PersonReadDto> customSearch(String name, int age) {
        final List<Person> entities = repository.findUsingMyCustomRule(name, age);
        return toReadDtos(entities);
    }

    public int create(PersonWriteDto dto) {
        final Person entity = new Person(null, dto.getName(), dto.getAge());
        repository.save(entity);

        return entity.getId();
    }

    public boolean update(int id, PersonWriteDto dto) {
        final Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) {
            //If this method or this class is annotated with @Transactional and we modify the entity directly, we don't
            //even need to call repository.save again; Hibernate will detect the changes and save them automatically.
            final Person entity = optional.get();
            entity.setName(dto.getName());
            entity.setAge(dto.getAge());

            //If the method is not transactional, we must save it manually
            repository.save(entity);

            return true;
        } else {
            return false;
        }
    }

    public boolean delete(int id) {
        final Optional<Person> optional = repository.findById(id);
        if (optional.isPresent()) {
            repository.delete(optional.get());

            return true;
        } else {
            return false;
        }
    }
}
