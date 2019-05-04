package br.hikarikun92.restdemo.person;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

//Now, the repository is an interface extending JpaRepository. All the implementations will be done via Spring, and we
//rarely (if not never) will have to implement something manually.
public interface PersonRepository extends JpaRepository<Person, Integer> {
    //Based on the name of the method, Spring can guess what we want and implement a corresponding method
    List<Person> findAllByNameContaining(String name);

    //If it would be hard to guess or if the query needs something specific, we can manually implement it using the JPQL language
    @Query("select p from Person p where name not in ('Lucas', 'Elvis') and lower(name) like %:name% and age > :age")
    List<Person> findUsingMyCustomRule(String name, int age);
}
