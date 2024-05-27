package org.example.restwithspringbootandjavaerudio.unittests.mapper.mocks;

import org.example.restwithspringbootandjavaerudio.data.vo.v1.BookVO;
import org.example.restwithspringbootandjavaerudio.model.Book;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {
    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookVO mockVO() {
        return mockVO(0);
    }

    public List<Book> mockEntityList() {
        List<Book> Books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            Books.add(mockEntity(i));
        }
        return Books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> Books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            Books.add(mockVO(i));
        }
        return Books;
    }

    public Book mockEntity(Integer number) {
        Book Book = new Book();
        Book.setTitle("Title Test" + number);
        Book.setAuthor(((number % 2) == 0) ? "Rodrigo" : "Rafaela");
        Book.setId(number.longValue());
        Book.setPrice(number.doubleValue());
        Book.setLaunchDate(new Date());
        return Book;
    }

    public BookVO mockVO(Integer number) {
        BookVO Book = new BookVO();
        Book.setTitle("Title Test" + number);
        Book.setAuthor(((number % 2) == 0) ? "Rodrigo" : "Rafaela");
        Book.setKey(number.longValue());
        Book.setPrice(number.doubleValue());
        Book.setLaunchDate(new Date());
        return Book;
    }
}
