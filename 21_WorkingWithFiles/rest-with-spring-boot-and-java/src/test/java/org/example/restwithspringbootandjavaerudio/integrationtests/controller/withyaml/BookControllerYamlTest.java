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
public class BookControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper mapper;

    private static BookVO book;

    @BeforeAll
    public static void setup() {
        mapper = new YMLMapper();
        book = new BookVO();
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

        var createdBook = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(book, mapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, mapper);

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

        var persistedBook = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .pathParam("id", book.getKey())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, mapper);

        book = persistedBook;
        assertNotNull(persistedBook);

        assertNotNull(persistedBook.getKey());
        assertNotNull(persistedBook.getAuthor());
        assertNotNull(persistedBook.getTitle());
        assertNotNull(persistedBook.getPrice());
        assertNotNull(persistedBook.getLaunchDate());

        assertTrue(persistedBook.getKey() > 0);

        assertEquals("Rodrigo", persistedBook.getAuthor());
        assertEquals("Ja fui", persistedBook.getTitle());
        assertEquals(100.0, persistedBook.getPrice());
    }

    @Test
    @Order(3)
    public void testUpdate() throws JsonProcessingException {
        book.setTitle("Santana");

        var createdBook = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .body(book, mapper)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(BookVO.class, mapper);

        book = createdBook;
        assertNotNull(createdBook);

        assertNotNull(createdBook.getKey());
        assertNotNull(createdBook.getAuthor());
        assertNotNull(createdBook.getTitle());
        assertNotNull(createdBook.getPrice());
        assertNotNull(createdBook.getLaunchDate());

        assertEquals(book.getKey(), createdBook.getKey());

        assertEquals("Rodrigo", createdBook.getAuthor());
        assertEquals("Santana", createdBook.getTitle());
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
    public void testFindAll() {
        var wrapper = given()
                .spec(specification)
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(PagedModelBook.class, mapper);

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
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
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
                .config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
                .contentType(TestConfigs.CONTENT_TYPE_YML)
                .accept(TestConfigs.CONTENT_TYPE_YML)
                .queryParams("page", 0, "size", 12, "direction", "asc")
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();


        assertTrue(content.contains("rel: \"first\"\n" +
                "  href: \"http://localhost:8888/api/book/v1?direction=title%3A%20ASC&page=0&size=12&sort=title,asc\""));
        assertTrue(content.contains("rel: \"self\"\n" +
                "  href: \"http://localhost:8888/api/book/v1?page=0&size=12&direction=title%3A%20ASC\""));
        assertTrue(content.contains("rel: \"next\"\n" +
                "  href: \"http://localhost:8888/api/book/v1?direction=title%3A%20ASC&page=1&size=12&sort=title,asc\""));
        assertTrue(content.contains("rel: \"last\"\n" +
                "  href: \"http://localhost:8888/api/book/v1?direction=title%3A%20ASC&page=5&size=12&sort=title,asc\""));

        assertTrue(content.contains("rel: \"self\"\n" +
                "    href: \"http://localhost:8888/api/book/v1/46\""));

        assertTrue(content.contains("rel: \"self\"\n" +
                "    href: \"http://localhost:8888/api/book/v1/56\""));

        assertTrue(content.contains("""
                page:
                  size: 12
                  totalElements: 63
                  totalPages: 6
                  number: 0"""));
    }

    private void mockBook() {
        book.setAuthor("Rodrigo");
        book.setLaunchDate(new Date());
        book.setPrice(100.0);
        book.setTitle("Ja fui");
    }
}