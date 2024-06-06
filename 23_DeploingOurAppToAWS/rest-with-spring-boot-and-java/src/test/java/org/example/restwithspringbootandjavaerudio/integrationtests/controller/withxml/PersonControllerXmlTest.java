package org.example.restwithspringbootandjavaerudio.integrationtests.controller.withxml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.example.restwithspringbootandjavaerudio.configs.TestConfigs;
import org.example.restwithspringbootandjavaerudio.integrationtests.testcontainers.AbstractIntegrationTest;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.AccountCredentialsVO;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.PersonVO;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.TokenVO;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.pagemoidels.PagedModelPerson;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        objectMapper = new XmlMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonVO();
    }

    @Test
    @Order(0)
    public void authorization() {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        person = createdPerson;
        assertNotNull(createdPerson);

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.isEnabled());

        assertTrue(createdPerson.getId() > 0);

        assertEquals("Rodrigo", createdPerson.getFirstName());
        assertEquals("Santos", createdPerson.getLastName());
        assertEquals("Uberlandia, MG", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonProcessingException {
        person.setLastName("Hora");

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .body(person)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO createdPerson = objectMapper.readValue(content, PersonVO.class);
        person = createdPerson;
        assertNotNull(createdPerson);

        assertNotNull(createdPerson.getId());
        assertNotNull(createdPerson.getFirstName());
        assertNotNull(createdPerson.getLastName());
        assertNotNull(createdPerson.getAddress());
        assertNotNull(createdPerson.getGender());
        assertTrue(createdPerson.isEnabled());

        assertEquals(person.getId(), createdPerson.getId());

        assertEquals("Rodrigo", createdPerson.getFirstName());
        assertEquals("Hora", createdPerson.getLastName());
        assertEquals("Uberlandia, MG", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(3)
    public void testDisablePersonById() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;
        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertFalse(persistedPerson.isEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Rodrigo", persistedPerson.getFirstName());
        assertEquals("Hora", persistedPerson.getLastName());
        assertEquals("Uberlandia, MG", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(4)
    public void testFindById() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PersonVO persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;
        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertFalse(persistedPerson.isEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Rodrigo", persistedPerson.getFirstName());
        assertEquals("Hora", persistedPerson.getLastName());
        assertEquals("Uberlandia, MG", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(5)
    public void testDelete() {
        given()
                .spec(specification)
                .pathParam("id", person.getId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(6)
    public void testFindAll() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        var people = wrapper.getContent();
        PersonVO foundPersonOne = people.getFirst();


        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        assertTrue(foundPersonOne.isEnabled());


        assertEquals(627, foundPersonOne.getId());

        assertEquals("Alexina", foundPersonOne.getFirstName());
        assertEquals("Samme", foundPersonOne.getLastName());
        assertEquals("213 Sherman Crossing", foundPersonOne.getAddress());
        assertEquals("Female", foundPersonOne.getGender());

        PersonVO foundPersonSecond = people.get(1);
        person = foundPersonSecond;

        assertNotNull(foundPersonSecond.getId());
        assertNotNull(foundPersonSecond.getFirstName());
        assertNotNull(foundPersonSecond.getLastName());
        assertNotNull(foundPersonSecond.getAddress());
        assertNotNull(foundPersonSecond.getGender());
        assertFalse(foundPersonSecond.isEnabled());

        assertEquals(174, foundPersonSecond.getId());

        assertEquals("Alexis", foundPersonSecond.getFirstName());
        assertEquals("Boldison", foundPersonSecond.getLastName());
        assertEquals("62 Doe Crossing Alley", foundPersonSecond.getAddress());
        assertEquals("Female", foundPersonSecond.getGender());
    }

    @Test
    @Order(7)
    public void testFindAllWithoutToken() {
        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given()
                .spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    @Test
    @Order(8)
    public void testFindByName() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("firstName", "and")
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get("findPersonByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelPerson wrapper = objectMapper.readValue(content, PagedModelPerson.class);
        var people = wrapper.getContent();

        PersonVO foundPersonOne = people.getFirst();
        person = foundPersonOne;

        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        assertFalse(foundPersonOne.isEnabled());

        assertEquals(974, foundPersonOne.getId());

        assertEquals("Alejandro", foundPersonOne.getFirstName());
        assertEquals("O'Haire", foundPersonOne.getLastName());
        assertEquals("78 Iowa Circle", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());
    }

    @Test
    @Order(9)
    public void testHATEOAS() {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


//        assertTrue(content.contains("<links><rel>self</rel><href>http://localhost/api/person/v1/627</href></links>"));
//        assertTrue(content.contains("<links><rel>self</rel><href>http://localhost/api/person/v1/382</href></links>"));
//        assertTrue(content.contains("<links><rel>self</rel><href>http://localhost/api/person/v1/564</href></links>"));
//
//        assertTrue(content.contains("<links><rel>first</rel><href>http://localhost/api/person/v1?direction=firstName%3A%20ASC&amp;page=0&amp;size=10&amp;sort=firstName,asc</href></links>"));
//        assertTrue(content.contains("<links><rel>prev</rel><href>http://localhost/api/person/v1?direction=firstName%3A%20ASC&amp;page=2&amp;size=10&amp;sort=firstName,asc</href></links>"));
//        assertTrue(content.contains("<links><rel>self</rel><href>http://localhost/api/person/v1?page=3&amp;size=10&amp;direction=firstName%3A%20ASC</href></links>"));
//        assertTrue(content.contains("<links><rel>next</rel><href>http://localhost/api/person/v1?direction=firstName%3A%20ASC&amp;page=4&amp;size=10&amp;sort=firstName,asc</href></links>"));
//        assertTrue(content.contains("<links><rel>last</rel><href>http://localhost/api/person/v1?direction=firstName%3A%20ASC&amp;page=100&amp;size=10&amp;sort=firstName,asc</href></links>"));
//
//        assertTrue(content.contains("<page><size>10</size><totalElements>1010</totalElements><totalPages>101</totalPages><number>3</number></page>"));
    }

    private void mockPerson() {
        person.setFirstName("Rodrigo");
        person.setLastName("Santos");
        person.setAddress("Uberlandia, MG");
        person.setGender("Male");
        person.setEnabled(true);
    }
}