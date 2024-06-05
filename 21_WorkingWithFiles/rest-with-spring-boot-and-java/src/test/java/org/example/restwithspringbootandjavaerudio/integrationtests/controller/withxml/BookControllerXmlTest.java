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
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.BookVO;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.TokenVO;
import org.example.restwithspringbootandjavaerudio.integrationtests.vo.pagemoidels.PagedModelBook;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {

    public static BookVO book;
    private static RequestSpecification specification;
    private static XmlMapper objectMapper;

    @BeforeAll
    public static void setup() {
        objectMapper = new XmlMapper();
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
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
                .accept(TestConfigs.CONTENT_TYPE_XML)
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
    public void testFindAll() throws JsonProcessingException {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        PagedModelBook wrapper = objectMapper.readValue(content, PagedModelBook.class);
        var book = wrapper.getContent();
        BookVO foundBookOne = book.getFirst();


        assertNotNull(foundBookOne.getKey());
        assertNotNull(foundBookOne.getAuthor());
        assertNotNull(foundBookOne.getTitle());
        assertNotNull(foundBookOne.getPrice());
        assertNotNull(foundBookOne.getLaunchDate());

        assertEquals(46, foundBookOne.getKey());

        assertEquals("Nelie Bebbell", foundBookOne.getAuthor());
        assertEquals("Adventures of the Wilderness Family, The", foundBookOne.getTitle());
        assertEquals(61.3, foundBookOne.getPrice());

        BookVO foundBookSecond = book.get(3);
        BookControllerXmlTest.book = foundBookSecond;

        assertNotNull(foundBookSecond.getKey());
        assertNotNull(foundBookSecond.getAuthor());
        assertNotNull(foundBookSecond.getTitle());
        assertNotNull(foundBookSecond.getPrice());
        assertNotNull(foundBookSecond.getLaunchDate());

        assertEquals(54, foundBookSecond.getKey());

        assertEquals("Kalvin Dungate", foundBookSecond.getAuthor());
        assertEquals("Believers, The", foundBookSecond.getTitle());
        assertEquals(2.46, foundBookSecond.getPrice());
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


    @Test
    @Order(7)
    public void testHATEOAS() {
        var content = given()
                .spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_XML)
                .accept(TestConfigs.CONTENT_TYPE_XML)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


        assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1/46</href></links>"));
        assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1/56</href></links>"));

        assertTrue(content.contains("<links><rel>first</rel><href>http://localhost:8888/api/book/v1?direction=title%3A%20ASC&amp;page=0&amp;size=12&amp;sort=title,asc</href></links>"));

        assertTrue(content.contains("<links><rel>self</rel><href>http://localhost:8888/api/book/v1?page=0&amp;size=12&amp;direction=title%3A%20ASC</href></links>"));

        assertTrue(content.contains("<links><rel>next</rel><href>http://localhost:8888/api/book/v1?direction=title%3A%20ASC&amp;page=1&amp;size=12&amp;sort=title,asc</href></links>"));

        assertTrue(content.contains("<links><rel>last</rel><href>http://localhost:8888/api/book/v1?direction=title%3A%20ASC&amp;page=5&amp;size=12&amp;sort=title,asc</href></links>"));

        assertTrue(content.contains("<page><size>12</size><totalElements>63</totalElements><totalPages>6</totalPages><number>0</number></page>"));
    }

    private void mockBook() {
        book.setAuthor("Rodrigo");
        book.setLaunchDate(new Date());
        book.setPrice(100.0);
        book.setTitle("Ja fui");
    }
}