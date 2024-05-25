package org.example.restwithspringbootandjavaerudio.services;

import org.example.restwithspringbootandjavaerudio.data.vo.v1.PersonVO;
import org.example.restwithspringbootandjavaerudio.exceptions.ResourceNotFoundException;
import org.example.restwithspringbootandjavaerudio.mapper.ModelMapper;
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

    public List<PersonVO> findAll() {
        logger.info("Finding all people");
        return ModelMapper.parseListObjects(repository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person");

        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        return ModelMapper.parseObject(entity, PersonVO.class);
    }

    public PersonVO create(PersonVO person) {
        logger.info("Creating one person");
        Person entity = ModelMapper.parseObject(person, Person.class);
        return ModelMapper.parseObject(repository.save(entity), PersonVO.class);
    }

    public PersonVO update(PersonVO person) {
        logger.info("Updating one person!");

        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return ModelMapper.parseObject(repository.save(entity), PersonVO.class);

    }

    public void delete(Long id) {
        logger.info("Deleting one person");
        Person entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        repository.delete(entity);
    }
}
