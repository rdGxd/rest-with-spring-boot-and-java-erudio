package org.example.restwithspringbootandjavaerudio.services;

import org.example.restwithspringbootandjavaerudio.data.dtos.v1.PersonDTO;
import org.example.restwithspringbootandjavaerudio.exceptions.ResourceNotFoundException;
import org.example.restwithspringbootandjavaerudio.model.Person;
import org.example.restwithspringbootandjavaerudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonServices {
    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    public PersonRepository repository;

    public List<PersonDTO> findAll() {
        logger.info("Finding all people");
        List<PersonDTO> list = new ArrayList<>();
        for (Person p : repository.findAll()) {
            list.add(new PersonDTO(p.getId(), p.getFirstName(), p.getLastName(), p.getAddress(), p.getGender()));
        }
        return list;
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one person");
        Person person = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        return new PersonDTO(person.getId(), person.getFirstName(), person.getLastName(), person.getAddress(), person.getGender());
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating one person");
        Person entity = new Person();
        entity.setFirstName(person.firstName());
        entity.setLastName(person.lastName());
        entity.setAddress(person.address());
        entity.setGender(person.gender());
        repository.save(entity);
        return person;
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating one person");
        Person entity = repository.findById(person.id()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.firstName());
        entity.setLastName(person.lastName());
        entity.setAddress(person.address());
        entity.setGender(person.gender());
        repository.save(entity);
        return person;
    }

    public void delete(Long id) {
        logger.info("Deleting one person");
        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        repository.delete(entity);
    }
}
