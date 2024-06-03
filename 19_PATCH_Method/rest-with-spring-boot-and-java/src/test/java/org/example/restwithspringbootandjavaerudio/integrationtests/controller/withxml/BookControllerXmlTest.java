package org.example.restwithspringbootandjavaerudio.integrationtests.controller.withxml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.BookVO;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.TokenVO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    private static BookVO book;
    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.getDefault());

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        book = new BookVO();
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
                .setBasePath("/api/book/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockBook();

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO createdBook = objectMapper.readValue(content, BookVO.class);
        book = createdBook;
        assertNotNull(createdBook);

        assertNotNull(createdBook.getKey());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());

        assertTrue(createdBook.getKey() > 0);

        assertEquals("Rodrigo", createdBook.getAuthor());
        assertEquals("Ja fui", createdBook.getTitle());
        assertEquals(100.0, createdBook.getPrice());
    }

    @Test
    @Order(2)
    public void testFindById() throws JsonProcessingException {
        mockBook();

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .pathParam("id", book.getKey())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO persistedPerson = objectMapper.readValue(content, BookVO.class);
        book = persistedPerson;
        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getKey());
        assertNotNull(persistedPerson.getAuthor());
        assertNotNull(persistedPerson.getAuthor());
        assertNotNull(persistedPerson.getPrice());
        assertNotNull(persistedPerson.getLaunchDate());

        assertTrue(persistedPerson.getKey() > 0);

        assertEquals("Rodrigo", persistedPerson.getAuthor());
        assertEquals("Ja fui", persistedPerson.getTitle());
        assertEquals(100.0, persistedPerson.getPrice());
    }

    @Test
    @Order(3)
    public void testUpdate() throws JsonProcessingException {
        book.setTitle("Voltei");

        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .body(book)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        BookVO createdBook = objectMapper.readValue(content, BookVO.class);
        book = createdBook;
        assertNotNull(createdBook);

        assertNotNull(createdBook.getKey());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());

        assertEquals(book.getKey(), createdBook.getKey());

        assertEquals("Rodrigo", createdBook.getAuthor());
        assertEquals("Voltei", createdBook.getTitle());
        assertEquals(100.0, createdBook.getPrice());
    }

    @Test
    @Order(4)
    public void testDelete() {
        given()
                .spec(specification)
                .pathParam("id", book.getKey())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonProcessingException, ParseException {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<BookVO> people = objectMapper.readValue(content, new TypeReference<List<BookVO>>() {
        });

        BookVO foundPersonOne = people.getFirst();
        book = foundPersonOne;

        assertNotNull(foundPersonOne.getKey());
        assertNotNull(foundPersonOne.getAuthor());
        assertNotNull(foundPersonOne.getAuthor());
        assertNotNull(foundPersonOne.getPrice());
        assertNotNull(foundPersonOne.getLaunchDate());

        assertEquals(1, foundPersonOne.getKey());

        assertEquals("Michael C. Feathers", foundPersonOne.getAuthor());
        assertEquals("Working effectively with legacy code", foundPersonOne.getTitle());
        assertEquals(49.00, foundPersonOne.getPrice());
        Date date = inputFormat.parse("2017-11-28 22:00:00.000000");
        assertEquals(date, foundPersonOne.getLaunchDate());

        BookVO foundPersonSecond = people.get(3);
        book = foundPersonSecond;

        assertNotNull(foundPersonSecond.getKey());
        assertNotNull(foundPersonSecond.getAuthor());
        assertNotNull(foundPersonSecond.getAuthor());
        assertNotNull(foundPersonSecond.getPrice());
        assertNotNull(foundPersonSecond.getLaunchDate());

        assertEquals(4, foundPersonSecond.getKey());

        assertEquals("Crockford", foundPersonSecond.getAuthor());
        assertEquals("JavaScript", foundPersonSecond.getTitle());
        assertEquals(67.00, foundPersonSecond.getPrice());
    }

    @Test
    @Order(6)
    public void testFindAllWithoutToken() {
        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/book/v1")
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

    private void mockBook() {
        book.setAuthor("Rodrigo");
        book.setLaunchDate(new Date());
        book.setPrice(100.0);
        book.setTitle("Ja fui");
    }
}