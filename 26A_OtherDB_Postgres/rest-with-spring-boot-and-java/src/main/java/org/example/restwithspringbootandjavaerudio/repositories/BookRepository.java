package org.example.restwithspringbootandjavaerudio.repositories;

import org.example.restwithspringbootandjavaerudio.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
