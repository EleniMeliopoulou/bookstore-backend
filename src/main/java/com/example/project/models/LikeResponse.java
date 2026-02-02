package com.example.project.models;

public class LikeResponse {
	private Books book;
    private boolean liked;
    
	public LikeResponse(Books book, boolean liked) {
		super();
		this.book = book;
		this.liked = liked;
	}

	public Books getBook() {
		return book;
	}

	public void setBook(Books book) {
		this.book = book;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}
    
	
    
}
