package com.example.project.controllers.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.project.models.Books;

public class UserStripped {
	private Long id;
	private String username;
	private String email;
	private List<String> likedBooks = new ArrayList<>();
	
	public UserStripped(Long id, String username, String email,List<String> likedBooks) {
		super();
        this.id = id;
        this.username = username;
        this.email = email;
    }
	public UserStripped(Long id, String username, String email) {
		super();
        this.id = id;
        this.username = username;
        this.email = email;
    }
	
	public List<String> getLikedBooks() {
		return likedBooks;
	}
	public void setLikedBooks(List<String> likedBooks) {
		this.likedBooks = likedBooks;
	}
	public UserStripped(String username) {
		super();
		this.username = username;
	}
	
	public UserStripped() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
