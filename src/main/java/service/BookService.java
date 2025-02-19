package service;

import dto.BookDTO;
import entity.Book;
import mapper.BookMapper;
import repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private final BookRepository repository = new BookRepository();

    public BookDTO createBook(BookDTO dto) {
        Book book = BookMapper.toBook(dto);
        Book created = repository.create(book);
        return BookMapper.toBookDTO(created);
    }

    public BookDTO getBookById(int id) {
        Book book = repository.findByBookId(id);
        return BookMapper.toBookDTO(book);
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = repository.findAll();
        return books.stream().map(BookMapper::toBookDTO).collect(Collectors.toList());
    }

    public boolean updateBook(int id, BookDTO dto) {
        Book book = BookMapper.toBook(dto);
        book.setBook_id(id);
        return repository.update(book);
    }

    public boolean deleteBook(int id) {
        return repository.delete(id);
    }
}
