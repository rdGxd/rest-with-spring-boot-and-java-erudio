package org.example.restwithspringbootandjavaerudio.services;

import org.example.restwithspringbootandjavaerudio.exceptions.ResourceNotFoundException;
import org.example.restwithspringbootandjavaerudio.model.Person;
import org.example.restwithspringbootandjavaerudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    public PersonRepository repository;


    public List<Person> findAll() {
        logger.info("Finding all people");
        return repository.findAll();
    }

    public Person findById(Long id) {
        logger.info("Finding one person");
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
    }

    public Person create(Person person) {
        logger.info("Creating one person");
        return repository.save(person);
    }

    public Person update(Person person) {
        logger.info("Updating one person");
        Person entity = findById(person.getId());

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        return repository.save(person);
    }

    public void delete(Long id) {
        logger.info("Deleting one person");
        Person entity = findById(id);
        repository.delete(entity);
    }
}
