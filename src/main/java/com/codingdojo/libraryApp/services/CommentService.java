package com.codingdojo.libraryApp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.codingdojo.libraryApp.models.Book;
import com.codingdojo.libraryApp.models.Comment;
import com.codingdojo.libraryApp.repositories.CommentRepository;

@Service
public class CommentService {
	
	@Autowired
	private CommentRepository commentRepo;
	
	public Comment addComment(Comment newComment, BindingResult result) {
    	
		// Return null if result has errors
    	if(result.hasErrors()) {
    		return null;
    	}
    	
    	newComment = commentRepo.save(newComment);
    	System.out.println("New comment created with ID: " + newComment.getId());
    	
    	return newComment;

	}
	
	public List<Comment> getCommentsBook(Book book) {
		
		return commentRepo.findAllByBook(book);
	}
	
	public Comment findById(Long id) {
		
		Optional<Comment> result = commentRepo.findById(id);
		if(result.isPresent()) {
			return result.get();
		}
		
		return null;
	}
	
	public void deleteComment(Comment comment) {
		
		commentRepo.delete(comment);
	}

}
