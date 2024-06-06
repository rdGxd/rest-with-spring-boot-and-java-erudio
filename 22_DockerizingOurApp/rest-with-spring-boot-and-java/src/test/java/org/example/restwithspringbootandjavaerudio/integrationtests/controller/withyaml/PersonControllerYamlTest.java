package org.example.restwithspringbootandjavaerudio.integrationtests.controller.withyaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.example.restwithspringbootandjavaerudio.configs.TestConfigs;
import org.example.restwithspringbootandjavaerudio.integrationtests.controller.withyaml.mapper.YMLMapper;
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
public class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper mapper;

    private static PersonVO person;

    @BeforeAll
    public static void setup() {
        mapper = new YMLMapper();
        person = new PersonVO();
    }

    @Test
    @Order(0)
    public void authorization() {
        AccountCredentialsVO user = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given().config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .basePath("/auth/signin")
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(user, mapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenVO.class, mapper)
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

        var persistedPerson = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(person, mapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class, mapper);


        person = persistedPerson;
        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertTrue(persistedPerson.isEnabled());

        assertTrue(persistedPerson.getId() > 0);

        assertEquals("Rodrigo", persistedPerson.getFirstName());
        assertEquals("Santos", persistedPerson.getLastName());
        assertEquals("Uberlandia, MG", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(2)
    public void testUpdate() {
        person.setLastName("Santana");

        var createdPerson = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(person, mapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class, mapper);

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
        assertEquals("Santana", createdPerson.getLastName());
        assertEquals("Uberlandia, MG", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
    }

    @Test
    @Order(3)
    public void testDisablePersonById() {
        mockPerson();

        var persistedPerson = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", person.getId())
                .when()
                .patch("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class, mapper);

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
        assertEquals("Santana", persistedPerson.getLastName());
        assertEquals("Uberlandia, MG", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(4)
    public void testFindById() throws JsonProcessingException {
        mockPerson();

        var persistedPerson = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PersonVO.class, mapper);

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
        assertEquals("Santana", persistedPerson.getLastName());
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
    public void testFindAll() {
        var wrapper = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelPerson.class, mapper);

        var people = wrapper.getContent();

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
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    @Test
    @Order(8)
    public void testFindByName() {
        var wrapper = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("firstName", "and")
                .queryParams("page", 0, "size", 10, "direction", "asc")
                .when()
                .get("findPersonByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelPerson.class, mapper);

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
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


//        assertTrue(content.contains("rel: \"first\"\n" +
//                "  href: \"http://localhost/api/person/v1?direction=firstName%3A%20ASC&page=0&size=10&sort=firstName,asc\""));
//        assertTrue(content.contains("rel: \"prev\"\n" +
//                "  href: \"http://localhost/api/person/v1?direction=firstName%3A%20ASC&page=2&size=10&sort=firstName,asc\""));
//        assertTrue(content.contains("rel: \"self\"\n" +
//                "  href: \"http://localhost/api/person/v1?page=3&size=10&direction=firstName%3A%20ASC\""));
//        assertTrue(content.contains("rel: \"next\"\n" +
//                "  href: \"http://localhost/api/person/v1?direction=firstName%3A%20ASC&page=4&size=10&sort=firstName,asc\""));
//        assertTrue(content.contains("rel: \"last\"\n" +
//                "  href: \"http://localhost/api/person/v1?direction=firstName%3A%20ASC&page=100&size=10&sort=firstName,asc\""));
//
//        assertTrue(content.contains("rel: \"self\"\n" +
//                "    href: \"http://localhost/api/person/v1/627\""));
//
//        assertTrue(content.contains("rel: \"self\"\n" +
//                "    href: \"http://localhost/api/person/v1/174\""));
//
//        assertTrue(content.contains("rel: \"self\"\n" +
//                "    href: \"http://localhost/api/person/v1/382\""));
//
//        assertTrue(content.contains("""
//                page:
//                  size: 10
//                  totalElements: 1009
//                  totalPages: 101
//                  number: 3"""));
    }

    private void mockPerson() {
        person.setFirstName("Rodrigo");
        person.setLastName("Santos");
        person.setAddress("Uberlandia, MG");
        person.setGender("Male");
        person.setEnabled(true);
    }
}