package br.hikarikun92.restdemo.person;

import br.hikarikun92.restdemo.exception.DataException;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//A repository class is mainly responsible for managing the data to be used by the application. Such classes can, for
//example, retrieve data from a database, from memory, from a file etc.
@Repository
public class PersonRepository {
    private final DataSource dataSource;

    public PersonRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    //Retrieve all the people present in this system
    public List<Person> getAll() {
        try (Connection connection = dataSource.getConnection()) {
            final ResultSet resultSet = connection.createStatement().executeQuery("select id, name, age from person");

            List<Person> people = new ArrayList<>();
            while (resultSet.next()) {
                final int id = resultSet.getInt("id");
                final String name = resultSet.getString("name");
                final int age = resultSet.getInt("age");

                Person person = new Person(id, name, age);
                people.add(person);
            }

            return people;
        } catch (SQLException e) {
            throw new DataException("Error retrieving people list", e);
        }
    }

    //Retrieve a person by its name, or none if no such name exists. This method returns an Optional, indicating the person
    //might or not be found in the system.
    public Optional<Person> getByName(String name) {
        try (Connection connection = dataSource.getConnection()) {
            //We will use a PreparedStatement both for optimization and because it can sanitize user input, avoiding, for example, SQL injection
            final PreparedStatement preparedStatement = connection.prepareStatement("select id, age from person where name = ?");
            preparedStatement.setString(1, name);

            final ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                final int id = resultSet.getInt("id");
                final int age = resultSet.getInt("age");

                Person person = new Person(id, name, age);
                return Optional.of(person);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataException("Error searching for person " + name, e);
        }
    }
}
