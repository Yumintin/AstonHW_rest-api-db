package entity;

import lombok.Data;

@Data
public class Book {
    private Integer book_id;
    private String title;
    private Integer author_id;
    private Integer year_published;
    private String genre;
}