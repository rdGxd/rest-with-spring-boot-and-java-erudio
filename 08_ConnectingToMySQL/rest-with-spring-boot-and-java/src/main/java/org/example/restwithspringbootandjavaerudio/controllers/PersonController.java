package org.example.restwithspringbootandjavaerudio.controllers;

import org.example.restwithspringbootandjavaerudio.model.Person;
import org.example.restwithspringbootandjavaerudio.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "/person")
public class PersonController {

    @Autowired
    private PersonServices service;
    // Path é OBRIGATÓRIO (/sum/10/20)
    // Query é OPCIONAL (/sum?numberOne=10&numberTwo=20)

    @GetMapping
    public List<Person> findAll() {
        return service.findAll();
    }

    @GetMapping(value = "/{id}")
    public Person findById(@PathVariable(value = "id") String id) {
        return service.findById(id);
    }

    @PostMapping
    public Person create(@RequestBody Person person) {
        return service.create(person);
    }

    @PutMapping
    public Person update(@RequestBody Person person) {
        return service.update(person);
    }

    @DeleteMapping(value = "/{id}")
    public void delete(@PathVariable(value = "id") String id) {
        service.delete(id);
    }
}

