package dto;

import lombok.Data;

import java.util.Objects;

@Data
public class BookDTO {
    private Integer bookId;
    private String title;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookDTO bookDTO = (BookDTO) o;
        return Objects.equals(bookId, bookDTO.bookId) && Objects.equals(title, bookDTO.title) && Objects.equals(authorId, bookDTO.authorId) && Objects.equals(yearPublished, bookDTO.yearPublished) && Objects.equals(genre, bookDTO.genre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, title, authorId, yearPublished, genre);
    }

    private Integer authorId;
    private Integer yearPublished;
    private String genre;
}