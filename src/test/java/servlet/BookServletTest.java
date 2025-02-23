package servlet;

import dto.BookDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BookService;
import util.JsonUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServletTest {

    private BookServlet servlet;
    private BookService bookServiceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new BookServlet();
        bookServiceMock = mock(BookService.class);
        Field serviceField = BookServlet.class.getDeclaredField("bookService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, bookServiceMock);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testDoGet_WithId_Success() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");

        BookDTO bookDTO = new BookDTO();
        bookDTO.setBookId(1);
        bookDTO.setTitle("Book Title");
        bookDTO.setAuthorId(10);
        bookDTO.setYearPublished(2020);
        bookDTO.setGenre("Fiction");

        when(bookServiceMock.getBookById(1)).thenReturn(bookDTO);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"bookId\":1"));
        assertTrue(output.contains("\"genre\":\"Fiction\""));
    }

    @Test
    void testDoGet_WithId_InvalidFormat() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/abc");

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String output = responseWriter.toString();
        assertTrue(output.contains("Invalid book ID format"));
    }

    @Test
    void testDoGet_WithId_NotFound() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/5");
        when(bookServiceMock.getBookById(5)).thenReturn(null);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Book not found"));
    }

    @Test
    void testDoGet_NoId_ReturnsAllBooks() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(null);
        List<BookDTO> books = Arrays.asList(new BookDTO(), new BookDTO());
        when(bookServiceMock.getAllBooks()).thenReturn(books);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.trim().startsWith("["));
    }

    @Test
    void testDoPost_Success() throws ServletException, IOException {
        String jsonInput = "{\"title\":\"New Book\", \"authorId\":10, \"yearPublished\":2021, \"genre\":\"Sci-Fi\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        BookDTO createdDto = new BookDTO();
        createdDto.setBookId(2);
        createdDto.setTitle("New Book");
        createdDto.setAuthorId(10);
        createdDto.setYearPublished(2021);
        createdDto.setGenre("Sci-Fi");

        when(bookServiceMock.createBook(any(BookDTO.class))).thenReturn(createdDto);

        servlet.doPost(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"bookId\":2"));
    }

    @Test
    void testDoPut_Success() throws ServletException, IOException {
        String jsonInput = "{\"bookId\":1,\"title\":\"Updated Book\", \"authorId\":10, \"yearPublished\":2022, \"genre\":\"Drama\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(bookServiceMock.updateBook(eq(1), any(BookDTO.class))).thenReturn(true);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"bookId\":1"));
        assertTrue(output.contains("Updated Book"));
    }

    @Test
    void testDoPut_NotFound() throws ServletException, IOException {
        String jsonInput = "{\"bookId\":999,\"title\":\"Not Found\", \"authorId\":10, \"yearPublished\":2020, \"genre\":\"Unknown\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(bookServiceMock.updateBook(eq(999), any(BookDTO.class))).thenReturn(false);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Book not found"));
    }

    @Test
    void testDoDelete_Success() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(bookServiceMock.deleteBook(1)).thenReturn(true);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("Book deleted"));
    }

    @Test
    void testDoDelete_InvalidId() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/abc");

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String output = responseWriter.toString();
        assertTrue(output.contains("Invalid book ID format"));
    }

    @Test
    void testDoDelete_NotFound() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/99");
        when(bookServiceMock.deleteBook(99)).thenReturn(false);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Book not found"));
    }
}
