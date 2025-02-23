package servlet;

import dto.AuthorDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.AuthorService;
import util.JsonUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorServletTest {

    private AuthorServlet servlet;
    private AuthorService authorServiceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new AuthorServlet();
        authorServiceMock = mock(AuthorService.class);
        Field serviceField = AuthorServlet.class.getDeclaredField("authorService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, authorServiceMock);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testDoGet_WithId_Success() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("1");

        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setAuthorId(1);
        authorDTO.setName("John Doe");
        authorDTO.setBirthYear(1970);

        when(authorServiceMock.getAuthorById(1)).thenReturn(authorDTO);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"authorId\":1"));
        assertTrue(output.contains("\"birthYear\":1970"));
    }

    @Test
    void testDoGet_WithId_InvalidFormat() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("abc");

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String output = responseWriter.toString();
        assertTrue(output.contains("Invalid ID format"));
    }

    @Test
    void testDoGet_WithId_NotFound() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn("5");
        when(authorServiceMock.getAuthorById(5)).thenReturn(null);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Author not found"));
    }

    @Test
    void testDoGet_NoId_ReturnsAllAuthors() throws ServletException, IOException {
        when(request.getParameter("id")).thenReturn(null);
        List<AuthorDTO> authors = Arrays.asList(new AuthorDTO(), new AuthorDTO());
        when(authorServiceMock.getAllAuthors()).thenReturn(authors);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.trim().startsWith("["));
    }

    @Test
    void testDoPost_Success() throws ServletException, IOException {
        String jsonInput = "{\"name\":\"Jane Doe\", \"birthYear\":1985}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        AuthorDTO createdDto = new AuthorDTO();
        createdDto.setAuthorId(2);
        createdDto.setName("Jane Doe");
        createdDto.setBirthYear(1985);

        when(authorServiceMock.createAuthor(any(AuthorDTO.class))).thenReturn(createdDto);

        servlet.doPost(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"authorId\":2"));
    }

    @Test
    void testDoPut_Success() throws ServletException, IOException {
        String jsonInput = "{\"authorId\":1,\"name\":\"John Updated\", \"birthYear\":1975}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(authorServiceMock.updateAuthor(eq(1), any(AuthorDTO.class))).thenReturn(true);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"authorId\":1"));
        assertTrue(output.contains("John Updated"));
    }

    @Test
    void testDoPut_NotFound() throws ServletException, IOException {
        String jsonInput = "{\"authorId\":999,\"name\":\"Not Found\", \"birthYear\":0}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(authorServiceMock.updateAuthor(eq(999), any(AuthorDTO.class))).thenReturn(false);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Author not found"));
    }

    @Test
    void testDoDelete_Success() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/authors/1");
        when(authorServiceMock.deleteAuthor(1)).thenReturn(true);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("Author deleted"));
    }

    @Test
    void testDoDelete_InvalidId() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/authors/abc");

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String output = responseWriter.toString();
        assertTrue(output.contains("Invalid ID format"));
    }

    @Test
    void testDoDelete_NotFound() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/api/v1/authors/99");
        when(authorServiceMock.deleteAuthor(99)).thenReturn(false);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Author not found"));
    }
}
