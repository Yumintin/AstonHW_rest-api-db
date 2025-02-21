package dto;

import dto.AuthorDTO;
import dto.BookDTO;
import dto.LoanDTO;
import dto.ReaderDTO;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class DtoTest {

    // Тест для AuthorDTO
    @Test
    public void testAuthorDTOEqualsAndHashCode() {
        AuthorDTO author1 = new AuthorDTO();
        author1.setAuthorId(1);
        author1.setName("Author One");
        author1.setBirthYear(1975);

        AuthorDTO author2 = new AuthorDTO();
        author2.setAuthorId(1);
        author2.setName("Author One");
        author2.setBirthYear(1975);

        AuthorDTO author3 = new AuthorDTO();
        author3.setAuthorId(2);
        author3.setName("Author Two");
        author3.setBirthYear(1980);

        // Проверка equals
        assertEquals(author1, author2); // Ожидаем, что они равны
        assertNotEquals(author1, author3); // Ожидаем, что они не равны

        // Проверка hashCode
        assertEquals(author1.hashCode(), author2.hashCode()); // hashCode должен быть одинаковым для равных объектов
        assertNotEquals(author1.hashCode(), author3.hashCode()); // hashCode должен быть разным для разных объектов
    }

    // Тест для BookDTO
    @Test
    public void testBookDTOEqualsAndHashCode() {
        BookDTO book1 = new BookDTO();
        book1.setBookId(1);
        book1.setTitle("Book One");
        book1.setAuthorId(1);
        book1.setYearPublished(2000);
        book1.setGenre("Fiction");

        BookDTO book2 = new BookDTO();
        book2.setBookId(1);
        book2.setTitle("Book One");
        book2.setAuthorId(1);
        book2.setYearPublished(2000);
        book2.setGenre("Fiction");

        BookDTO book3 = new BookDTO();
        book3.setBookId(2);
        book3.setTitle("Book Two");
        book3.setAuthorId(1);
        book3.setYearPublished(2001);
        book3.setGenre("Non-fiction");

        // Проверка equals
        assertEquals(book1, book2); // Ожидаем, что они равны
        assertNotEquals(book1, book3); // Ожидаем, что они не равны

        // Проверка hashCode
        assertEquals(book1.hashCode(), book2.hashCode()); // hashCode должен быть одинаковым для равных объектов
        assertNotEquals(book1.hashCode(), book3.hashCode()); // hashCode должен быть разным для разных объектов
    }

    // Тест для LoanDTO
    @Test
    public void testLoanDTOEqualsAndHashCode() {
        LoanDTO loan1 = new LoanDTO();
        loan1.setLoanId(1);
        loan1.setBookId(1);
        loan1.setReaderId(1);
        loan1.setLoanDate("2025-01-01");
        loan1.setReturnDate("2025-01-15");

        LoanDTO loan2 = new LoanDTO();
        loan2.setLoanId(1);
        loan2.setBookId(1);
        loan2.setReaderId(1);
        loan2.setLoanDate("2025-01-01");
        loan2.setReturnDate("2025-01-15");

        LoanDTO loan3 = new LoanDTO();
        loan3.setLoanId(2);
        loan3.setBookId(2);
        loan3.setReaderId(2);
        loan3.setLoanDate("2025-02-01");
        loan3.setReturnDate("2025-02-15");

        // Проверка equals
        assertEquals(loan1, loan2); // Ожидаем, что они равны
        assertNotEquals(loan1, loan3); // Ожидаем, что они не равны

        // Проверка hashCode
        assertEquals(loan1.hashCode(), loan2.hashCode()); // hashCode должен быть одинаковым для равных объектов
        assertNotEquals(loan1.hashCode(), loan3.hashCode()); // hashCode должен быть разным для разных объектов
    }

    // Тест для ReaderDTO
    @Test
    public void testReaderDTOEqualsAndHashCode() {
        ReaderDTO reader1 = new ReaderDTO();
        reader1.setReaderId(1);
        reader1.setName("Reader One");
        reader1.setEmail("reader1@example.com");
        reader1.setRegistrationDate("2025-01-01");

        ReaderDTO reader2 = new ReaderDTO();
        reader2.setReaderId(1);
        reader2.setName("Reader One");
        reader2.setEmail("reader1@example.com");
        reader2.setRegistrationDate("2025-01-01");

        ReaderDTO reader3 = new ReaderDTO();
        reader3.setReaderId(2);
        reader3.setName("Reader Two");
        reader3.setEmail("reader2@example.com");
        reader3.setRegistrationDate("2025-02-01");

        // Проверка equals
        assertEquals(reader1, reader2); // Ожидаем, что они равны
        assertNotEquals(reader1, reader3); // Ожидаем, что они не равны

        // Проверка hashCode
        assertEquals(reader1.hashCode(), reader2.hashCode()); // hashCode должен быть одинаковым для равных объектов
        assertNotEquals(reader1.hashCode(), reader3.hashCode()); // hashCode должен быть разным для разных объектов
    }
}
