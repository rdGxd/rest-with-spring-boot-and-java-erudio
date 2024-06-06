package org.example.restwithspringbootandjavaerudio.integrationtests.repositories;

import org.example.restwithspringbootandjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import org.example.restwithspringbootandjavaerudio.model.Person;
import org.example.restwithspringbootandjavaerudio.repositories.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTest {

    private static Person person;
    @Autowired
    public PersonRepository repository;

    @BeforeAll
    public static void setup() {
        person = new Person();
    }

    @Test
    @Order(0)
    public void testFindByName() {
        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonsByName("and", pageable).getContent().get(5);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertTrue(person.isEnabled());

        assertEquals(445, person.getId());

        assertEquals("Andris", person.getFirstName());
        assertEquals("Dutnall", person.getLastName());
        assertEquals("51232 Scoville Junction", person.getAddress());
        assertEquals("Male", person.getGender());
    }

    @Test
    @Order(1)
    public void testDisablePerson() {
        repository.disablePerson(person.getId());
        Pageable pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonsByName("and", pageable).getContent().get(5);

        assertNotNull(person.getId());
        assertNotNull(person.getFirstName());
        assertNotNull(person.getLastName());
        assertNotNull(person.getAddress());
        assertNotNull(person.getGender());
        assertFalse(person.isEnabled());

        assertEquals(445, person.getId());

        assertEquals("Andris", person.getFirstName());
        assertEquals("Dutnall", person.getLastName());
        assertEquals("51232 Scoville Junction", person.getAddress());
        assertEquals("Male", person.getGender());
    }
}
