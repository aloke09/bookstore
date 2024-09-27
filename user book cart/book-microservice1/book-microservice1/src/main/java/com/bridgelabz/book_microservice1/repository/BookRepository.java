package com.bridgelabz.book_microservice1.repository;

import com.bridgelabz.book_microservice1.model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Books, Long>
{
    Optional<Books> findByBookName(String book);
}
