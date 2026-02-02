package com.example.project.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.project.models.Books;
import com.example.project.models.LikeResponse;
import com.example.project.models.User;
import com.example.project.repositories.BooksRepository;
import com.example.project.repositories.UserRepository;
import jakarta.transaction.Transactional;

@Service
public class LibraryService {

	@Autowired UserRepository userRepository;
	@Autowired BooksRepository booksRepository;
	@Autowired private PasswordEncoder passwordEncoder;
	
	//Users
	@Transactional
	public User createUser(String username, String email, String password) {
		String hashedPassword = passwordEncoder.encode(password);
	    User user = new User(username, email, hashedPassword);
	    return userRepository.save(user);
	}
	
	public User getUserByEmail(String email) {
		Optional<User> user = userRepository.findByEmail(email);
		if(!user.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " Not found");
		return user.get();
	}
	
	@Transactional
	public User updateUser(String email, String username) {
	    Optional<User> userOpt = userRepository.findByEmail(email);
	    if (!userOpt.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " Not found");
	    }
	    User user = userOpt.get();
	    if (username != null) {
	        user.setUsername(username);
	    }
	    return user;
	}
	
	@Transactional
	public User changePassword(String email, String password) {
	    Optional<User> userOpt = userRepository.findByEmail(email);
	    if (!userOpt.isPresent()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " Not found");
	    }
	    User user = userOpt.get();
	    if (password != null) {
	    	String hashedPassword = passwordEncoder.encode(password);
	        user.setPassword(hashedPassword);
	    }
	    return user;
	}
	
	public List<Books> getLikedBooks(Long userId) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new RuntimeException("User not found"));

	    return user.getLikedBooks(); 
	}
	
	public List<String> getLikedBooksTitles(Long userId) {
	    User user = userRepository.findById(userId)
	        .orElseThrow(() -> new RuntimeException("User not found"));

	    return user.getLikedBooks().stream().map(Books::getTitle).toList(); 
	}
	
	//Books
	public Books getBook(Long id) {
		Optional<Books> book = booksRepository.findById(id);
		if(!book.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id " + id + " Not found");
		return book.get();
	}
	
	public List<Books> getAllBooks() {
		return booksRepository.findAll();
	}
	
	public List<Books> searchBook(String title) {
		return booksRepository.findByTitleContainingIgnoreCase(title);
	}
	
	public Books addBook(Books book){
		return booksRepository.save(book);
	}
	
	public List<Books> removeBook(Long id){
		Optional<Books> book = booksRepository.findById(id);
		if(!book.isPresent())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book with id " + id + " Not found");
		booksRepository.deleteById(id);
		return booksRepository.findAll();
	}
	
	@Transactional
	public LikeResponse toggleLikeBook(Long userId,Long bookId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
		Books book = booksRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));
		
		boolean isLiked; 
		
		if(book.isLiked() && book.getLikedBy() != null && book.getLikedBy().getId().equals(userId)) {
			//Unlike
			book.setLikedBy(null);
			book.setLiked(false);
			user.getLikedBooks().remove(book);
			isLiked = false;
		}else {
			//Like
			book.setLikedBy(user);
			book.setLiked(true);
			user.getLikedBooks().add(book);
			isLiked = true;
		}
		
		booksRepository.save(book);
		
		return new LikeResponse(book,isLiked);
	}
	
	

}
