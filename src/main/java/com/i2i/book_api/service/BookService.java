package com.i2i.book_api.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import com.i2i.book_api.dto.BookDto;

@Service
public class BookService {

    private final JdbcTemplate jdbcTemplate;

    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Gelen ham metni işleyip veritabanına kaydeder
    public void importBooks(String rawData) {
        
        String xmlQuery = "SELECT BOOK_OPERATIONS.parse_to_xml(?) FROM DUAL";
        String xmlData = jdbcTemplate.queryForObject(xmlQuery, String.class, rawData);

        String jsonQuery = "SELECT BOOK_OPERATIONS.parse_to_json(?) FROM DUAL";
        String jsonData = jdbcTemplate.queryForObject(jsonQuery, String.class, rawData);

        String callProcedure = "CALL BOOK_OPERATIONS.import_books(?, ?)";
        jdbcTemplate.update(callProcedure, xmlData, jsonData);
    }

    // Veritabanındaki tüm kitapları Cursor ile çeker
    public List<BookDto> getAllBooks() {
        
        String sql = "{CALL BOOK_OPERATIONS.get_all_books(?)}";

        return jdbcTemplate.execute(
            (Connection con) -> {
                CallableStatement cs = con.prepareCall(sql);
                cs.registerOutParameter(1, Types.REF_CURSOR);
                return cs;
            },
            (CallableStatement cs) -> {
                cs.execute();
                List<BookDto> books = new ArrayList<>();
                
                // try-with-resources kullanarak ResultSet'in otomatik kapanmasını sağlıyoruz
                try (ResultSet rs = (ResultSet) cs.getObject(1)) {
                    while (rs.next()) {
                        BookDto dto = new BookDto();
                        dto.setTitle(rs.getString("title"));
                        dto.setAuthorName(rs.getString("author_name"));
                        dto.setPublisherName(rs.getString("publisher_name"));
                        books.add(dto);
                    }
                }
                return books;
            }
        );
    }
}