package br.hikarikun92.restdemo.person;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//A repository class is mainly responsible for managing the data to be used by the application. Such classes can, for
//example, retrieve data from a database, from memory, from a file etc.
@Repository
public class PersonRepository {
    //Use a local field as an in-memory data storage
    private final List<Person> existingPeople = List.of(
            new Person("Lucas", 27),
            new Person("Fulano", 30),
            new Person("Siclano", 22)
    );

    //Retrieve all the people present in this system
    public List<Person> getAll() {
        return existingPeople;
    }

    //Retrieve a person by its name, or none if no such name exists. This method returns an Optional, indicating the person
    //might or not be found in the system.
    public Optional<Person> getByName(String name) {
        for (Person person : existingPeople) {
            if (person.getName().equals(name)) {
                return Optional.of(person);
            }
        }

        return Optional.empty();
    }
}
