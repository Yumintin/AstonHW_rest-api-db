package entity;

import lombok.Data;

@Data
public class Book {
    private Integer bookId;
    private String title;
    private Integer authorId;
    private Integer yearPublished;
    private String genre;
}