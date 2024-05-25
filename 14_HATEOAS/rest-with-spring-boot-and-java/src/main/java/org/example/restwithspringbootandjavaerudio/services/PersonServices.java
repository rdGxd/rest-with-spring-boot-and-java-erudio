package org.example.restwithspringbootandjavaerudio.services;

import org.example.restwithspringbootandjavaerudio.controllers.PersonController;
import org.example.restwithspringbootandjavaerudio.data.vo.v1.PersonVO;
import org.example.restwithspringbootandjavaerudio.exceptions.RequiredObjectIsNullException;
import org.example.restwithspringbootandjavaerudio.exceptions.ResourceNotFoundException;
import org.example.restwithspringbootandjavaerudio.mapper.Mapper;
import org.example.restwithspringbootandjavaerudio.model.Person;
import org.example.restwithspringbootandjavaerudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    private final Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    public PersonRepository repository;

    public List<PersonVO> findAll() {
        logger.info("Finding all people");
        List<PersonVO> personVOS = Mapper.parseListObjects(repository.findAll(), PersonVO.class);
        personVOS.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return personVOS;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person");

        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        PersonVO vo = Mapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {
        logger.info("Creating one person");
        if (person == null) throw new RequiredObjectIsNullException();

        Person entity = Mapper.parseObject(person, Person.class);
        PersonVO vo = Mapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updating one person!");

        if (person == null) throw new RequiredObjectIsNullException();

        Person entity = repository.findById(person.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        PersonVO vo = Mapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person");
        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        repository.delete(entity);
    }
}
