package com.codingdojo.libraryApp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.codingdojo.libraryApp.models.Book;
import com.codingdojo.libraryApp.repositories.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepo;
	
	public Book addBook(Book newBook, BindingResult result) {
		
		// Reject if title is taken (present in database)
    	Optional<Book> bookLookUp = bookRepo.findByTitle(newBook.getTitle());
    	if (bookLookUp.isPresent()) {
    		System.out.println(bookLookUp);
    		result.rejectValue("name", "Unique", "Book with this title already exists.");
    	}
    	
    	// Return null if result has errors
    	if(result.hasErrors()) {
    		return null;
    	}
    	
    	newBook = bookRepo.save(newBook);
    	System.out.println("New book created with ID: " + newBook.getId());
    	
        return newBook;
	}
	
	public Book updateBook(Book book) {
		
		return bookRepo.save(book);
	}
	
	public List<Book> getAllBooks() {
		return bookRepo.findAllByOrderByTitleAsc();
	}
	
	public Book findById(Long id) {
		
		Optional<Book> result = bookRepo.findById(id);
		if(result.isPresent()) {
			return result.get();
		}
		
		return null;
	}
	
	public void deleteBook(Book book) {
		
		bookRepo.delete(book);
	}
}
