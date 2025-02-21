package mapper;

import dto.BookDTO;
import entity.Book;

public class BookMapper implements Mapper<Book, BookDTO> {
    @Override
    public BookDTO toDto(Book book) {
        if (book == null) {
            return null;
        }
        BookDTO dto = new BookDTO();
        dto.setBookId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setAuthorId(book.getAuthorId());
        dto.setYearPublished(book.getYearPublished());
        dto.setGenre(book.getGenre());
        return dto;
    }

    @Override
    public Book toEntity(BookDTO dto) {
        if (dto == null) {
            return null;
        }
        Book book = new Book();
        book.setBookId(dto.getBookId());
        book.setTitle(dto.getTitle());
        book.setAuthorId(dto.getAuthorId());
        book.setYearPublished(dto.getYearPublished());
        book.setGenre(dto.getGenre());
        return book;
    }
}
