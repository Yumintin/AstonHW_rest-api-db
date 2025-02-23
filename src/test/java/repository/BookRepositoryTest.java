package repository;

import entity.Book;
import org.junit.jupiter.api.*;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookRepositoryTest {

    private BookRepository bookRepository;

    @BeforeAll
    public void setUp() {
        bookRepository = new BookRepository();
    }

    @BeforeEach
    public void clearData() {

    }

    @Test
    public void testCreateAndFindByBookId() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthorId(1);
        book.setYearPublished(2020);
        book.setGenre("Fiction");

        Book createdBook = bookRepository.create(book);
        Assertions.assertNotNull(createdBook, "Созданный объект Book не должен быть null");
        Assertions.assertNotNull(createdBook.getBookId(), "ID книги должен быть установлен после создания");

        Book foundBook = bookRepository.findByBookId(createdBook.getBookId());
        Assertions.assertNotNull(foundBook, "Книга должна быть найдена по ID");
        Assertions.assertEquals("Test Book", foundBook.getTitle());
        Assertions.assertEquals(1, foundBook.getAuthorId());
        Assertions.assertEquals(2020, foundBook.getYearPublished());
        Assertions.assertEquals("Fiction", foundBook.getGenre());
    }

    @Test
    public void testUpdate() {
        Book book = new Book();
        book.setTitle("Original Book");
        book.setAuthorId(1);
        book.setYearPublished(2019);
        book.setGenre("Sci-fi");

        Book createdBook = bookRepository.create(book);
        createdBook.setTitle("Updated Book");
        createdBook.setYearPublished(2021);

        boolean updated = bookRepository.update(createdBook);
        Assertions.assertTrue(updated, "Обновление должно пройти успешно");

        Book updatedBook = bookRepository.findByBookId(createdBook.getBookId());
        Assertions.assertNotNull(updatedBook, "После обновления книга должна находиться по ID");
        Assertions.assertEquals("Updated Book", updatedBook.getTitle());
        Assertions.assertEquals(2021, updatedBook.getYearPublished());
    }

    @Test
    public void testFindAll() {
        Book book1 = new Book();
        book1.setTitle("Book One");
        book1.setAuthorId(1);
        book1.setYearPublished(2000);
        book1.setGenre("Drama");
        bookRepository.create(book1);

        Book book2 = new Book();
        book2.setTitle("Book Two");
        book2.setAuthorId(2);
        book2.setYearPublished(2010);
        book2.setGenre("Mystery");
        bookRepository.create(book2);

        List<Book> books = bookRepository.findAll();
        Assertions.assertNotNull(books, "Список книг не должен быть null");
        Assertions.assertTrue(books.size() >= 2, "В базе должно быть минимум 2 книги");
    }

    @Test
    public void testDelete() {
        Book book = new Book();
        book.setTitle("Delete Book");
        book.setAuthorId(1);
        book.setYearPublished(2005);
        book.setGenre("Horror");

        Book createdBook = bookRepository.create(book);
        boolean deleted = bookRepository.delete(createdBook.getBookId());
        Assertions.assertTrue(deleted, "Удаление книги должно пройти успешно");

        Book foundBook = bookRepository.findByBookId(createdBook.getBookId());
        Assertions.assertNull(foundBook, "После удаления книга не должна быть найдена");
    }
}
