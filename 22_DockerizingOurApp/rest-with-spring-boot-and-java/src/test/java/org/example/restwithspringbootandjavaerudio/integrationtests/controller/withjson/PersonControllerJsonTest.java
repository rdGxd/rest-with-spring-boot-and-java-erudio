package org.example.restwithspringbootandjavaerudio.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.wrappers.WrapperPersonVO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
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

        assertEquals("Nelson", createdPerson.getFirstName());
        assertEquals("Santos", createdPerson.getLastName());
        assertEquals("Uberlandia, MG", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonProcessingException {
        person.setLastName("Silva");

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
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

        assertEquals("Nelson", createdPerson.getFirstName());
        assertEquals("Silva", createdPerson.getLastName());
        assertEquals("Uberlandia, MG", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(3)
    public void testDisablePersonById() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
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

        assertEquals(person.getId(), persistedPerson.getId());


        assertEquals("Nelson", persistedPerson.getFirstName());
        assertEquals("Silva", persistedPerson.getLastName());
        assertEquals("Uberlandia, MG", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(4)
    public void testFindById() throws JsonProcessingException {
        mockPerson();

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
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

        assertEquals(person.getId(), persistedPerson.getId());


        assertEquals("Nelson", persistedPerson.getFirstName());
        assertEquals("Silva", persistedPerson.getLastName());
        assertEquals("Uberlandia, MG", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
        assertFalse(persistedPerson.isEnabled());
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapper.getEmbedded().getPersons();

        PersonVO foundPersonOne = people.getFirst();
        person = foundPersonOne;

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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("firstName", "and")
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get("findPersonByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapper.getEmbedded().getPersons();

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
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


//        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost/api/person/v1/627\"}}"));
//        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost/api/person/v1/174\"}}"));
//        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost/api/person/v1/382\"}}"));
//
//        assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost/api/person/v1?direction=firstName%3A%20ASC&page=0&size=10&sort=firstName,asc\"}"));
//        assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost/api/person/v1?direction=firstName%3A%20ASC&page=2&size=10&sort=firstName,asc\"}"));
//        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost/api/person/v1?page=3&size=10&direction=firstName%3A%20ASC\"}"));
//        assertTrue(content.contains("\"next\":{\"href\":\"http://localhost/api/person/v1?direction=firstName%3A%20ASC&page=4&size=10&sort=firstName,asc\"}"));
//        assertTrue(content.contains("\"last\":{\"href\":\"http://localhost/api/person/v1?direction=firstName%3A%20ASC&page=100&size=10&sort=firstName,asc\"}}"));
//
//        assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1009,\"totalPages\":101,\"number\":3}}"));
    }

    private void mockPerson() {
        person.setFirstName("Nelson");
        person.setLastName("Santos");
        person.setAddress("Uberlandia, MG");
        person.setGender("Male");
        person.setEnabled(true);
    }
}