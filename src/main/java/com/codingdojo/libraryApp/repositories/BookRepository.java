package com.codingdojo.libraryApp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.libraryApp.models.Book;
import com.codingdojo.libraryApp.models.User;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
	
	Book findByIdIs(Long id);
	List<Book> findAll();
	List<Book> findAllByOrderByTitleAsc();
	Optional<Book> findByTitle(String title);
	List<Book> findAllByBorrower(User borrower);
	List<Book> findByBorrowerNotContains(User borrower);

}
