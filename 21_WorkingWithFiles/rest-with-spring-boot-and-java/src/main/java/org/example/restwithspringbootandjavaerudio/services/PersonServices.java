package org.example.restwithspringbootandjavaerudio.services;

import jakarta.validation.constraints.NotNull;
import org.example.restwithspringbootandjavaerudio.controllers.PersonController;
import org.example.restwithspringbootandjavaerudio.data.vo.v1.PersonVO;
import org.example.restwithspringbootandjavaerudio.exceptions.RequiredObjectIsNullException;
import org.example.restwithspringbootandjavaerudio.exceptions.ResourceNotFoundException;
import org.example.restwithspringbootandjavaerudio.mapper.MapperPerson;
import org.example.restwithspringbootandjavaerudio.model.Person;
import org.example.restwithspringbootandjavaerudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonServices {
    private final Logger logger = Logger.getLogger(PersonServices.class.getName());
    @Autowired
    public PagedResourcesAssembler<PersonVO> assembler;
    @Autowired
    private PersonRepository repository;

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        logger.info("Finding all people");

        var personPage = repository.findAll(pageable);
        return getEntityModels(pageable, personPage);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName, Pageable pageable) {
        logger.info("Finds people by name");

        var personPage = repository.findPersonsByName(firstName, pageable);
        return getEntityModels(pageable, personPage);
    }

    @NotNull
    private PagedModel<EntityModel<PersonVO>> getEntityModels(Pageable pageable, Page<Person> personPage) {
        var personVosPage = personPage.map(p -> MapperPerson.parseObject(repository.save(p), PersonVO.class));

        personVosPage.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().toString())).withSelfRel();

        return assembler.toModel(personVosPage, link);
    }


    public PersonVO findById(Long id) {
        logger.info("Finding one person");

        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        PersonVO vo = MapperPerson.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {
        logger.info("Creating one person");
        if (person == null) throw new RequiredObjectIsNullException();

        Person entity = MapperPerson.parseObject(person, Person.class);
        PersonVO vo = MapperPerson.parseObject(repository.save(entity), PersonVO.class);
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

        PersonVO vo = MapperPerson.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    @Transactional
    public PersonVO disablePerson(Long id) {
        logger.info("Disabling one person");
        repository.disablePerson(id);

        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        PersonVO vo = MapperPerson.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person");
        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        repository.delete(entity);
    }
}
