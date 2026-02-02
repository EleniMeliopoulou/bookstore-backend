package com.example.project.controllers;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.BookLoader;
import com.example.project.controllers.dto.UserStripped;
import com.example.project.models.Books;
import com.example.project.models.LikeResponse;
import com.example.project.models.Request;
import com.example.project.models.LoginResponse;
import com.example.project.models.User;
import com.example.project.services.LibraryService;

@CrossOrigin(origins = "*")
@RestController
public class LibraryController {
	
	@Autowired LibraryService libraryService;
	@Autowired private PasswordEncoder passwordEncoder;
	
	//Users
	@PostMapping("/createuser")
	public ResponseEntity<UserStripped> createUser(@RequestBody Map<String, String> body) {
	    String username = body.get("username");
	    String email = body.get("email");
	    String password = body.get("password");

	    if (username == null || username.trim().isEmpty() ||
	        email == null || email.trim().isEmpty() ||
	        password == null || password.trim().isEmpty()) {
	        return ResponseEntity.badRequest().body(null);
	    }

	    User user = libraryService.createUser(username, email, password);
	    return ResponseEntity.ok(new UserStripped(
	    		user.getId(), 
	    		user.getUsername(), 
	    		user.getEmail(),
	    		user.getLikedBooks().stream().map(Books::getTitle).toList()));
	}
	
	@PostMapping("/login") 
	public ResponseEntity<?> login(@RequestBody Request request) { 
		User user = libraryService.getUserByEmail(request.getEmail()); 
		if (user == null) { 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found"); 
			} 
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) { 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password"); 
			} 
		return ResponseEntity.ok(new UserStripped( 
				user.getId(), 
				user.getUsername(), 
				user.getEmail(), 
				user.getLikedBooks().stream().map(Books::getTitle).toList()));
	}	
	
	@GetMapping("/getuserbyemail")
	public UserStripped getUserByEmail(@RequestParam String email) {
	    User user = libraryService.getUserByEmail(email);
	    List<String> titles = user.getLikedBooks().stream().map(Books::getTitle).toList();
	    return new UserStripped(user.getId(), user.getUsername(), user.getEmail(),titles);
	}
	
	@PutMapping("/updateuser")
	public User updateUser(@RequestBody Map<String, String> body) {
		String email = body.get("email"); 
		String username = body.get("username"); 
		return libraryService.updateUser(email, username);
	}
	
	@PostMapping("/changepassword")
	public ResponseEntity<?> changePassword(@RequestBody Request  request) { 
		User user = libraryService.getUserByEmail(request.getEmail()); 
		
		if (user == null) { 
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found"); 
		}
		user.setPassword(request.getPassword());
		libraryService.changePassword(request.getEmail(), request.getPassword());
		return ResponseEntity.ok(Map.of("message", "Password changed!!!"));	
	}
	
	@GetMapping("/liked-books")
	public ResponseEntity<List<Books>> getLikedBooks(@RequestParam Long userId) {
	    List<Books> likedBooks = libraryService.getLikedBooks(userId);
	    return ResponseEntity.ok(likedBooks);
	}
	
	@GetMapping("/liked-books-titles")
	public ResponseEntity<List<String>> getLikedBooksTitles(@RequestParam Long userId) {
	    List<String> likedBooksTitles = libraryService.getLikedBooksTitles(userId);
	    return ResponseEntity.ok(likedBooksTitles);
	}
	
	//Books
	@GetMapping("/getbook")
	public Books getBook(@RequestParam Long id) {
		return libraryService.getBook(id);
	}
	
	@GetMapping("/getallbooks")
	public List<Books> getAllBooks(){
		return libraryService.getAllBooks().stream().limit(500).collect(Collectors.toList());
	}
	
	@GetMapping("/searchbook/{title}")
	public List<Books> searchBook(@PathVariable String title) {
		return libraryService.searchBook(title);	
	}
	
	@PostMapping("/addbook")
	public Books addBook(@RequestBody Books book) {
	    return libraryService.addBook(book);
	}
	
	@DeleteMapping("/deletebook")
	public List<Books> removeBook(@RequestParam Long id){
		return libraryService.removeBook(id);
	}
	
	@PostMapping("/toggle-like")
	public ResponseEntity<LikeResponse> toggleLike(@RequestParam Long userId, @RequestParam Long bookId) {
	    LikeResponse response = libraryService.toggleLikeBook(userId, bookId);
	    return ResponseEntity.ok(response);
	}
	
}
