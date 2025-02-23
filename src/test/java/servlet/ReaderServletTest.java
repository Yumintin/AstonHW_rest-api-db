package servlet;

import dto.ReaderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ReaderService;
import util.JsonUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReaderServletTest {

    private ReaderServlet servlet;
    private ReaderService readerServiceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new ReaderServlet();
        readerServiceMock = mock(ReaderService.class);
        Field serviceField = ReaderServlet.class.getDeclaredField("readerService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, readerServiceMock);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testDoGet_WithId_Success() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");

        ReaderDTO readerDTO = new ReaderDTO();
        readerDTO.setReaderId(1);
        readerDTO.setName("Alice");
        readerDTO.setEmail("alice@example.com");
        readerDTO.setRegistrationDate("2025-01-01");

        when(readerServiceMock.getReaderById(1)).thenReturn(readerDTO);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"readerId\":1"));
        assertTrue(output.contains("\"email\":\"alice@example.com\""));
    }

    @Test
    void testDoGet_WithId_InvalidFormat() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/abc");

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String output = responseWriter.toString();
        assertTrue(output.contains("Invalid reader ID format"));
    }

    @Test
    void testDoGet_WithId_NotFound() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/5");
        when(readerServiceMock.getReaderById(5)).thenReturn(null);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Reader not found"));
    }

    @Test
    void testDoGet_NoId_ReturnsAllReaders() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(null);
        List<ReaderDTO> readers = Arrays.asList(new ReaderDTO(), new ReaderDTO());
        when(readerServiceMock.getAllReaders()).thenReturn(readers);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.trim().startsWith("["));
    }

    @Test
    void testDoPost_Success() throws ServletException, IOException {
        String jsonInput = "{\"name\":\"Bob\", \"email\":\"bob@example.com\", \"registrationDate\":\"2025-02-20\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        ReaderDTO createdDto = new ReaderDTO();
        createdDto.setReaderId(2);
        createdDto.setName("Bob");
        createdDto.setEmail("bob@example.com");
        createdDto.setRegistrationDate("2025-02-20");

        when(readerServiceMock.createReader(any(ReaderDTO.class))).thenReturn(createdDto);

        servlet.doPost(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"readerId\":2"));
    }

    @Test
    void testDoPut_Success() throws ServletException, IOException {
        String jsonInput = "{\"readerId\":1,\"name\":\"Charlie Updated\", \"email\":\"charlie@example.com\", \"registrationDate\":\"2025-02-20\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(readerServiceMock.updateReader(eq(1), any(ReaderDTO.class))).thenReturn(true);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"readerId\":1"));
        assertTrue(output.contains("Charlie Updated"));
    }

    @Test
    void testDoPut_NotFound() throws ServletException, IOException {
        String jsonInput = "{\"readerId\":999,\"name\":\"Not Found\", \"email\":\"notfound@example.com\", \"registrationDate\":\"2025-01-01\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(readerServiceMock.updateReader(eq(999), any(ReaderDTO.class))).thenReturn(false);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Reader not found"));
    }

    @Test
    void testDoDelete_Success() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(readerServiceMock.deleteReader(1)).thenReturn(true);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("Reader deleted"));
    }

    @Test
    void testDoDelete_InvalidId() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/abc");

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String output = responseWriter.toString();
        assertTrue(output.contains("Invalid reader ID format"));
    }

    @Test
    void testDoDelete_NotFound() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/99");
        when(readerServiceMock.deleteReader(99)).thenReturn(false);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Reader not found"));
    }
}
