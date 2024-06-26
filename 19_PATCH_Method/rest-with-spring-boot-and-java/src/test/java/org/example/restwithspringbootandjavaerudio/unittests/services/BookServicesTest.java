package org.example.restwithspringbootandjavaerudio.unittests.services;


import org.example.restwithspringbootandjavaerudio.data.vo.v1.BookVO;
import org.example.restwithspringbootandjavaerudio.exceptions.RequiredObjectIsNullException;
import org.example.restwithspringbootandjavaerudio.model.Book;
import org.example.restwithspringbootandjavaerudio.repositories.BookRepository;
import org.example.restwithspringbootandjavaerudio.services.BookServices;
import org.example.restwithspringbootandjavaerudio.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServicesTest {

    MockBook input;
    @Mock
    private BookRepository repository;
    @InjectMocks
    private BookServices service;

    @BeforeEach
    void setUpMocks() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        BookVO result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Rafaela", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(1D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void testCreate() {
        Book persisted = input.mockEntity(1);
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.save(any(Book.class))).thenReturn(persisted);

        BookVO result = service.create(vo);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Rafaela", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(1D, result.getPrice());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void testCreateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.create(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    void testUpdate() {
        Book entity = input.mockEntity(1);

        entity.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setKey(1L);


        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        BookVO result = service.update(vo);

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Rafaela", result.getAuthor());
        assertEquals("Title Test1", result.getTitle());
        assertEquals(1D, result.getPrice());
        assertNotNull(result.getLaunchDate());

    }


    @Test
    void testUpdateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }

    @Test
    void testFindAll() {
        List<Book> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);

        List<BookVO> people = service.findAll();

        assertNotNull(people);
        assertEquals(14, people.size());

        BookVO BookOne = people.get(1);

        assertNotNull(BookOne);
        assertNotNull(BookOne.getKey());
        assertNotNull(BookOne.getLinks());

        assertTrue(BookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Rafaela", BookOne.getAuthor());
        assertEquals("Title Test1", BookOne.getTitle());
        assertEquals(1D, BookOne.getPrice());
        assertNotNull(BookOne.getLaunchDate());

        BookVO BookFour = people.get(4);

        assertNotNull(BookFour);
        assertNotNull(BookFour.getKey());
        assertNotNull(BookFour.getLinks());

        assertTrue(BookFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
        assertEquals("Rodrigo", BookFour.getAuthor());
        assertEquals("Title Test4", BookFour.getTitle());
        assertEquals(4D, BookFour.getPrice());
        assertNotNull(BookFour.getLaunchDate());

        BookVO BookSeven = people.get(7);

        assertNotNull(BookSeven);
        assertNotNull(BookSeven.getKey());
        assertNotNull(BookSeven.getLinks());

        assertTrue(BookSeven.toString().contains("links: [</api/book/v1/7>;rel=\"self\"]"));
        assertEquals("Rafaela", BookSeven.getAuthor());
        assertEquals("Title Test7", BookSeven.getTitle());
        assertEquals(7D, BookSeven.getPrice());
        assertNotNull(BookSeven.getLaunchDate());

    }

}
