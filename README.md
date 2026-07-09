# Book API Project

This project was created for the i2i Systems summer internship. It is a Spring Boot application that manages book records. It connects to a local Oracle database using Docker. The project uses Flyway for automatic database setup and PL/SQL for data processing.

## Technologies Used
* Java 21 & Spring Boot
* Oracle Database XE
* Docker & Docker Compose
* Flyway (Database Migrations)
* PL/SQL (Packages, Cursors, Triggers)

## How to Run the Project

1. Start the Oracle database using Docker:
```bash
docker compose up -d
```

2. Run the Spring Boot application from your IDE (VS Code) or terminal. Flyway will automatically create the tables and PL/SQL packages.

## API Endpoints

### 1. Import Books
Converts raw string data into XML/JSON using PL/SQL and saves it to the database.
* **Method:** POST
* **URL:** `http://localhost:8080/api/books/import`
* **Header:** `Content-Type: text/plain`
* **Body Format:** `Title;Author;Publisher|Title2;Author2;Publisher2`
* **Example Body:** `Sefiller;Victor Hugo;Can Yayinlari|1984;George Orwell;Can Yayinlari`

### 2. Get All Books
Fetches all books from the database using a PL/SQL Cursor and returns them as a JSON list.
* **Method:** GET
* **URL:** `http://localhost:8080/api/books`

## Database Structure
* **Tables:** AUTHORS, PUBLISHERS, BOOKS, AUDIT_LOGS
* **Automation:** A row-level trigger automatically logs the time and user in the `AUDIT_LOGS` table when a new book is inserted.
