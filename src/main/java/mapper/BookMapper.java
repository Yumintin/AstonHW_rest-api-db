package mapper;

import dto.BookDTO;
import entity.Book;

public class BookMapper {
    public static BookDTO toBookDTO(Book book) {
        if (book == null) return null;
        BookDTO bookDTO = new BookDTO();
        bookDTO.setBook_id(book.getBook_id());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor_id(book.getAuthor_id());
        bookDTO.setYear_published(book.getYear_published());
        bookDTO.setGenre(book.getGenre());
        return bookDTO;
    }

    public static Book toBook(BookDTO bookDTO) {
        if (bookDTO == null) return null;
        Book book = new Book();
        book.setBook_id(bookDTO.getBook_id());
        book.setTitle(bookDTO.getTitle());
        book.setAuthor_id(bookDTO.getAuthor_id());
        book.setYear_published(bookDTO.getYear_published());
        book.setGenre(bookDTO.getGenre());
        return book;
    }
}
