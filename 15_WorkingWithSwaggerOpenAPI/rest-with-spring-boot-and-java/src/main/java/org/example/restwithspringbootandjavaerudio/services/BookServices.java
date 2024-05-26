package org.example.restwithspringbootandjavaerudio.services;

import org.example.restwithspringbootandjavaerudio.controllers.BookController;
import org.example.restwithspringbootandjavaerudio.data.vo.v1.BookVO;
import org.example.restwithspringbootandjavaerudio.exceptions.RequiredObjectIsNullException;
import org.example.restwithspringbootandjavaerudio.exceptions.ResourceNotFoundException;
import org.example.restwithspringbootandjavaerudio.mapper.MapperBook;
import org.example.restwithspringbootandjavaerudio.model.Book;
import org.example.restwithspringbootandjavaerudio.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookServices {

    @Autowired
    private BookRepository repository;

    public List<BookVO> findAll() {
        List<BookVO> bookVOS = MapperBook.parseListObjects(repository.findAll(), BookVO.class);
        bookVOS.forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));
        return bookVOS;
    }

    public BookVO findById(Long id) {
        Book entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));

        BookVO bookVO = MapperBook.parseObject(entity, BookVO.class);
        bookVO.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return bookVO;
    }

    public BookVO create(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        Book entity = MapperBook.parseObject(book, Book.class);
        BookVO vo = MapperBook.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        Book entity = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + book.getKey()));

        entity.setAuthor(book.getAuthor());
        entity.setLaunch_date(book.getLaunch_date());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());
        BookVO vo = MapperBook.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        Book entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID: " + id));
        repository.delete(entity);
    }
}
