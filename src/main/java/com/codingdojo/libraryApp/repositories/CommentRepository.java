package com.codingdojo.libraryApp.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.codingdojo.libraryApp.models.Book;
import com.codingdojo.libraryApp.models.Comment;

@Repository
public interface CommentRepository extends CrudRepository<Comment, Long>{
	
	List<Comment> findAllByBook(Book book);
	
}