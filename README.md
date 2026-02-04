# Bookstore "Chapter One" Web Application - Backend API

This repository contains the backend of the **Bookstore "Chapter One"** web application. It provides RESTful APIs for user authentication, book management, likes, cart operations, and database initialization.

---

## Features

* **User Authentication:** User registration, login/logout, profile update (e.g., username).
* **Book Catalog:** Display a comprehensive list of books with details like Title, Author, Category, Publication Date, Ratings, and Description.
* **Categorized Browse:** Books are displayed in organized carousels for easy navigation.
* **Search Functionality:** Efficient search to find books by titles.
* **Cart System:** Users can add/remove books from the cart and proceed with their order.
* **Liked Books System:** Users can like/unlike books, view and manage their currently liked books.
  
---

## Technologies Used
   - **Java 17+**
   - **Spring Boot**
   - **Spring Security**
   - **Spring Data JPA**
   - **MySQL**
   - **Maven**

---

## Getting Started

### Prerequisites
- JDK 17+
- MySQL installed and running
- Maven

---

### Configure Database

Update `application.properties`:
```bash
spring.datasource.url=jdbc:mysql://localhost:3306/bookstore
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Load Books from CSV (First Run Only)

In `application.properties`:
```bash
bookloader.enabled=true
```

Run the application once.  

When the books finish loading into the database, set:
```bash
bookloader.enabled=false
```

This prevents duplicate entries.

---

## Running the Application
```bash
mvn spring-boot:run
```

The backend will start at:

http://localhost:8080

---

## Future Enhancements

### Persistent Cart System

A new feature will be added to store cart items in the database, similar to the liked-books system.

---

## Contribution

This backend was developed as part of a personal project and is an enhanced version of the academic project **"Online Library"**.
