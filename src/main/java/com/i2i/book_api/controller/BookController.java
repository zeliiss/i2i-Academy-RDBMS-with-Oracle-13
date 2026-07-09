package com.i2i.book_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import com.i2i.book_api.dto.BookDto;
import com.i2i.book_api.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Ayraçlı metni alarak veritabanına aktarır
    @PostMapping("/import")
    public ResponseEntity<String> importBooks(@RequestBody String rawData) {
        try {
            bookService.importBooks(rawData);
            return ResponseEntity.ok("Kitaplar başarıyla içe aktarıldı.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Kayıt sırasında hata oluştu: " + e.getMessage());
        }
    }

    // Tüm kitapları liste olarak döndürür
    @GetMapping
    public ResponseEntity<List<BookDto>> getBooks() {
        List<BookDto> books = bookService.getAllBooks();
        return ResponseEntity.ok(books);
    }
}