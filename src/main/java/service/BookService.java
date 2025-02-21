package service;

import dto.BookDTO;
import entity.Book;
import mapper.BookMapper;
import repository.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private final BookRepository repository = new BookRepository();
    private final BookMapper bookMapper = new BookMapper();

    public BookDTO createBook(BookDTO dto) {
        Book book = bookMapper.toEntity(dto);
        Book created = repository.create(book);
        return bookMapper.toDto(created);
    }

    public BookDTO getBookById(int id) {
        Book book = repository.findByBookId(id);
        return bookMapper.toDto(book);
    }

    public List<BookDTO> getAllBooks() {
        List<Book> books = repository.findAll();
        return books.stream().map(bookMapper::toDto).collect(Collectors.toList());
    }

    public boolean updateBook(int id, BookDTO dto) {
        Book book = bookMapper.toEntity(dto);
        book.setBookId(id);
        return repository.update(book);
    }

    public boolean deleteBook(int id) {
        return repository.delete(id);
    }
}
