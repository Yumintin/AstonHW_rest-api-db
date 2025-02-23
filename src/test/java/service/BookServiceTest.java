package service;

import dto.BookDTO;
import entity.Book;
import mapper.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.BookRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private BookRepository repository;
    private BookMapper bookMapper;
    private BookService bookService;

    @BeforeEach
    void setUp() {
        repository = mock(BookRepository.class);
        bookMapper = mock(BookMapper.class);
        bookService = new BookService(repository, bookMapper);
    }

    @Test
    void testCreateBook() {
        BookDTO inputDto = new BookDTO();
        inputDto.setTitle("Test Book");
        inputDto.setAuthorId(1);
        inputDto.setYearPublished(2020);
        inputDto.setGenre("Fiction");

        Book bookEntity = new Book();
        bookEntity.setTitle("Test Book");
        bookEntity.setAuthorId(1);
        bookEntity.setYearPublished(2020);
        bookEntity.setGenre("Fiction");

        Book createdBook = new Book();
        createdBook.setBookId(1);
        createdBook.setTitle("Test Book");
        createdBook.setAuthorId(1);
        createdBook.setYearPublished(2020);
        createdBook.setGenre("Fiction");

        BookDTO outputDto = new BookDTO();
        outputDto.setBookId(1);
        outputDto.setTitle("Test Book");
        outputDto.setAuthorId(1);
        outputDto.setYearPublished(2020);
        outputDto.setGenre("Fiction");

        when(bookMapper.toEntity(inputDto)).thenReturn(bookEntity);
        when(repository.create(bookEntity)).thenReturn(createdBook);
        when(bookMapper.toDto(createdBook)).thenReturn(outputDto);

        BookDTO result = bookService.createBook(inputDto);

        assertNotNull(result);
        assertEquals(1, result.getBookId());
        assertEquals("Test Book", result.getTitle());

        verify(bookMapper).toEntity(inputDto);
        verify(repository).create(bookEntity);
        verify(bookMapper).toDto(createdBook);
    }

    @Test
    void testGetBookById() {
        int id = 1;
        Book bookEntity = new Book();
        bookEntity.setBookId(id);
        bookEntity.setTitle("Test Book");

        BookDTO outputDto = new BookDTO();
        outputDto.setBookId(id);
        outputDto.setTitle("Test Book");

        when(repository.findByBookId(id)).thenReturn(bookEntity);
        when(bookMapper.toDto(bookEntity)).thenReturn(outputDto);

        BookDTO result = bookService.getBookById(id);

        assertNotNull(result);
        assertEquals(id, result.getBookId());
        assertEquals("Test Book", result.getTitle());

        verify(repository).findByBookId(id);
        verify(bookMapper).toDto(bookEntity);
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book();
        book1.setBookId(1);
        book1.setTitle("Book One");

        Book book2 = new Book();
        book2.setBookId(2);
        book2.setTitle("Book Two");

        List<Book> books = Arrays.asList(book1, book2);

        BookDTO dto1 = new BookDTO();
        dto1.setBookId(1);
        dto1.setTitle("Book One");

        BookDTO dto2 = new BookDTO();
        dto2.setBookId(2);
        dto2.setTitle("Book Two");

        when(repository.findAll()).thenReturn(books);
        when(bookMapper.toDto(book1)).thenReturn(dto1);
        when(bookMapper.toDto(book2)).thenReturn(dto2);

        List<BookDTO> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Book One", result.get(0).getTitle());
        assertEquals("Book Two", result.get(1).getTitle());

        verify(repository).findAll();
        verify(bookMapper).toDto(book1);
        verify(bookMapper).toDto(book2);
    }

    @Test
    void testUpdateBook() {
        int id = 1;
        BookDTO inputDto = new BookDTO();
        inputDto.setTitle("Updated Book");
        inputDto.setAuthorId(2);
        inputDto.setYearPublished(2021);
        inputDto.setGenre("Non-fiction");

        Book bookEntity = new Book();
        bookEntity.setTitle("Updated Book");
        bookEntity.setAuthorId(2);
        bookEntity.setYearPublished(2021);
        bookEntity.setGenre("Non-fiction");

        when(bookMapper.toEntity(inputDto)).thenReturn(bookEntity);
        when(repository.update(argThat(b -> b.getBookId() == id && "Updated Book".equals(b.getTitle()))))
                .thenReturn(true);

        boolean result = bookService.updateBook(id, inputDto);
        assertTrue(result);

        verify(bookMapper).toEntity(inputDto);
        verify(repository).update(argThat(b -> b.getBookId() == id && "Updated Book".equals(b.getTitle())));
    }

    @Test
    void testDeleteBook() {
        int id = 1;
        when(repository.delete(id)).thenReturn(true);
        boolean result = bookService.deleteBook(id);
        assertTrue(result);
        verify(repository).delete(id);
    }
}
