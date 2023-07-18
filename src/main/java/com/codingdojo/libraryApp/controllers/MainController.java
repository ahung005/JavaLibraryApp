package com.codingdojo.libraryApp.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.codingdojo.libraryApp.models.Book;
import com.codingdojo.libraryApp.models.Comment;
import com.codingdojo.libraryApp.models.User;
import com.codingdojo.libraryApp.services.BookService;
import com.codingdojo.libraryApp.services.CommentService;
import com.codingdojo.libraryApp.services.UserService;
import com.codingdojo.libraryApp.validator.UserValidator;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class MainController {
	
	private UserService userService;
	private UserValidator userValidator;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private CommentService commentService;
	
	public MainController(UserService userService, UserValidator userValidator) {
		this.userService = userService;
		this.userValidator = userValidator;
	}
	
	@RequestMapping("/register")
	public String registration(
			@Valid @ModelAttribute("user") User user, 
			BindingResult result, 
			Model model, 
			HttpSession session,
			HttpServletRequest request) {
		userValidator.validate(user, result);
		// Store the password before it is encrypted
		String password = user.getPassword();
		if(result.hasErrors()) {
			return "loginPage.jsp";
		}
		// Make first user SUPER ADMIN
		if(userService.allUsers().size()==0) {
			userService.newUser(user, "ROLE_SUPER_ADMIN");
		}else {
			userService.newUser(user, "ROLE_USER");
		}
		
		// Log in new user with the password we stored before encrypting it
		authWithHttpServletRequest(request, user.getEmail(), password);
		return "redirect:/";
	}
	
	// We will call this method to automatically log in newly registered users
	public void authWithHttpServletRequest(HttpServletRequest request, String email, String password) {
	    try {
	        request.login(email, password);
	    } catch (ServletException e) {
	    	System.out.println("Error while login: " + e);
	    }
	}
	
	@RequestMapping("/admin/{id}")
	public String makeAdmin(Principal principal, @PathVariable("id") Long id, Model model) {
		if(principal==null) {
			return "redirect:/login";
		}
		
		User user = userService.findById(id);
		userService.upgradeUser(user);
		
		model.addAttribute("users", userService.allUsers());
		 
		return "redirect:/home";
	}
	
	@RequestMapping("/user/{id}")
	public String makeUser(Principal principal, @PathVariable("id") Long id, Model model) {
		if(principal==null) {
			return "redirect:/login";
		}
		
		User user = userService.findById(id);
		userService.downgradeUser(user);
		
		model.addAttribute("users", userService.allUsers());
		 
		return "redirect:/home";
	}
	
	@RequestMapping("/login")
	public String login(
			@ModelAttribute("user") User user,
			@RequestParam(value="error", required=false) String error, 
			@RequestParam(value="logout", required=false) String logout, 
			Model model) {
		
		if(error!=null) {
			model.addAttribute("errorMessage","Invalid Credentials, Please try again.");
		}
		if(logout!=null) {
			model.addAttribute("logoutMessage","Logout Successful!");
		}
		
		return "loginPage.jsp";
	}
	
	@RequestMapping(value={"/", "/home"})
	public String home(Principal principal, Model model) {
		if(principal==null) {
			return "redirect:/login";
		}
		String email = principal.getName();
		User user = userService.findByEmail(email);
		model.addAttribute("user", user);
		model.addAttribute("books", bookService.getAllBooks());
		
		if(user!=null) {
			user.setLastLogin(new Date());
			userService.updateUser(user);
			// If the user is an ADMIN or SUPER_ADMIN they will be redirected to the admin page
			if(user.getRoles().get(0).getName().contains("ROLE_SUPER_ADMIN")||user.getRoles().get(0).getName().contains("ROLE_ADMIN")) {
				model.addAttribute("currentUser", userService.findByEmail(email));
				model.addAttribute("users", userService.allUsers());
				return "adminPage.jsp";
			}
			// All other users are redirected to the home page
		}
		System.out.println(principal.getName());
		return "home.jsp";
	}
	
	@RequestMapping("/delete/{id}")
	public String deleteUser(Principal principal, @PathVariable("id") Long id, HttpSession session, Model model) {	
		
		if(principal==null) {
			return "redirect:/login";
		}
		User user = userService.findById(id);
		userService.deleteUser(user);
		
		model.addAttribute("users", userService.allUsers());
		 
		return "redirect:/home";
	}
	
	@GetMapping("/book/new")
	public String newBook(Principal principal, @ModelAttribute("book") Book book, Model model) {
		
		if(principal==null) {
			return "redirect:/login";
		}
		
		return "newBook.jsp";
	}
	
	@PostMapping("/book")
	public String createBook(Principal principal, @Valid @ModelAttribute("book") Book book, BindingResult result) throws IOException {
		
		if(principal==null) {
			return "redirect:/login";
		}
		
		if (!book.getImageFile().isEmpty() && book.getImageFile() != null) {                
            byte[] bytes = book.getImageFile().getBytes();
            byte[] encodeBase64 = Base64.getEncoder().encode(bytes);
            String base64Encoded = new String(encodeBase64, "UTF-8");
            System.out.println(base64Encoded);
            book.setBase64imageFile(base64Encoded);
        }
        else {
        	// Sets default image to no cover
        	book.setBase64imageFile("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AAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNQAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAIkzUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACJM1AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAiTNAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJH/xAA9EAABAgMGAgcHBAICAQUAAAABAgQAA3EFERMzUpESUBAhMTRA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\n"
        			+ "");
        }
		
		System.out.println(book.getImageFile());
		bookService.addBook(book, result);
		
		if(result.hasErrors()) {
			return "newBook.jsp";
		}
		
		return "redirect:/home";
	}
	
	@GetMapping("/book/borrow/{id}")
	public String borrowBook(Principal principal, @PathVariable("id") Long id) {
		
		if(principal==null) {
			return "redirect:/login";
		}
		
		Book book = bookService.findById(id);
		User borrower = userService.findByEmail(principal.getName());
		
		book.setBorrower(borrower);
		
		bookService.updateBook(book);
		
		return "redirect:/home";
	}
	
	@GetMapping("/book/return/{id}")
	public String returnBook(Principal principal, @PathVariable("id") Long id) {
		
		if(principal==null) {
			return "redirect:/login";
		}
		
		Book book = bookService.findById(id);
		User borrower = userService.findByEmail(principal.getName());
		
		if(book.getBorrower() == borrower) {
			book.setBorrower(null);
			bookService.updateBook(book);
		}
		
		return "redirect:/home";
	}
	
	@GetMapping("/book/{id}")
	public String detailGame(Principal principal, @PathVariable("id") Long id, @ModelAttribute("newComment") Comment comment, Model model) {
    	
		if(principal==null) {
			return "redirect:/login";
		}
    	
    	Book book = bookService.findById(id);
    	User user = userService.findByEmail(principal.getName());
    	
    	
    	boolean isLiked = false;
    	if(book.getUserLikes().indexOf(user) != -1) {
    		isLiked = true;
    	}
    	
    	model.addAttribute("book", book);
    	model.addAttribute("user", user);
    	model.addAttribute("isLiked", isLiked);
    	model.addAttribute("likes", book.getUserLikes().size());
    	model.addAttribute("comments", commentService.getCommentsBook(book));
    	
    	return "detailBook.jsp";
	}
	
	@PostMapping("/book/{id}/addComment")
	public String addComment(Principal principal, @Valid @ModelAttribute("newComment") Comment comment, @PathVariable("id") Long id, BindingResult result) {
		
		if(principal==null) {
			return "redirect:/login";
		}
		
		System.out.println("Comment detail: " + comment.getWriting());
		if(comment.getWriting() == "") {
			return "redirect:/book/" + id;
		}
		
		System.out.println("Errors: " + result);
		commentService.addComment(comment, result);
		
		return "redirect:/book/" + id;
	}
	
	@GetMapping("/book/{id}/delete/{commentId}")
	public String deleteComment(Principal principal, @PathVariable("id") Long id, @PathVariable("commentId") Long commentId) {
		
		if(principal == null) {
    		return "redirect:/login";
    	}
		
		Comment comment = commentService.findById(commentId);
		commentService.deleteComment(comment);
		
		return "redirect:/book/" + id;
	}
	
	@GetMapping("/book/{id}/like") 
	public String likeBook(Principal principal, @PathVariable("id") Long id) {
		
		if(principal == null) {
    		return "redirect:/login";
    	}
		
		Book book = bookService.findById(id);
		User user = userService.findByEmail(principal.getName());
		
		List<User> likes = book.getUserLikes();
		
		if(likes.indexOf(user) == -1) {
			likes.add(user);
			book.setUserLikes(likes);
			
			bookService.updateBook(book);
		}
		
		return "redirect:/book/" + id;
	}
	
	@GetMapping("/book/{id}/unlike") 
	public String unlikeBook(Principal principal, @PathVariable("id") Long id) {
		
		if(principal == null) {
    		return "redirect:/login";
    	}
		
		Book book = bookService.findById(id);
		User user = userService.findByEmail(principal.getName());
		
		List<User> likes = book.getUserLikes();
		
		if(likes.indexOf(user) != -1) {
			likes.remove(user);
			book.setUserLikes(likes);
			
			bookService.updateBook(book);
		}
		
		return "redirect:/book/" + id;
	}
	
	@GetMapping("/book/{id}/edit")
	public String editBook(Principal principal, @PathVariable("id") Long id, Model model) {
		
		if(principal == null) {
			return "redirect:/login";
		}
		System.out.println(bookService.findById(id).getImageFile());
		model.addAttribute("book", bookService.findById(id));
		
		return "editBook.jsp";
	}
	
	@PutMapping("/book/{id}")
	public String updateBook(Principal principal, @Valid @ModelAttribute("book") Book book, BindingResult result, @RequestParam("file") MultipartFile file, @PathVariable("id") Long id) throws IOException {
		
		if(principal == null) {
    		return "redirect:/login";
    	}
    	
    	if(result.hasErrors()) {
    		return "editBook.jsp";
    	}
    	
    	Book book2 = bookService.findById(id);
    	if (!file.isEmpty()) {                
            byte[] bytes = file.getBytes();
            byte[] encodeBase64 = Base64.getEncoder().encode(bytes);
            String base64Encoded = new String(encodeBase64, "UTF-8");
            System.out.println(base64Encoded);
            book.setBase64imageFile(base64Encoded);
        }
    	else {
    		book.setBase64imageFile(book2.getBase64imageFile());
    	}
    	
    	book.setBorrower(book2.getBorrower());
    	book.setUserLikes(book2.getUserLikes());
    	
    	bookService.updateBook(book);
    	
		return "redirect:/home";
	}
	
	@GetMapping("/book/{id}/delete")
	public String deleteBook(Principal principal, @PathVariable("id") Long id) {
		
		if(principal == null) {
    		return "redirect:/login";
    	}
		
		Book book = bookService.findById(id);
		bookService.deleteBook(book);
		
		return "redirect:/home";
	}

}