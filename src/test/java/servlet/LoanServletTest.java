package servlet;

import dto.LoanDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.LoanService;
import util.JsonUtil;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServletTest {

    private LoanServlet servlet;
    private LoanService loanServiceMock;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter responseWriter;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new LoanServlet();
        loanServiceMock = mock(LoanService.class);
        Field serviceField = LoanServlet.class.getDeclaredField("loanService");
        serviceField.setAccessible(true);
        serviceField.set(servlet, loanServiceMock);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        responseWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testDoGet_WithId_Success() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoanId(1);
        loanDTO.setBookId(10);
        loanDTO.setReaderId(20);
        loanDTO.setLoanDate("2025-02-23");
        loanDTO.setReturnDate("2025-03-01");
        when(loanServiceMock.getLoanById(1)).thenReturn(loanDTO);

        servlet.doGet(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"loanId\":1"));
    }

    @Test
    void testDoGet_WithId_InvalidFormat() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/abc");
        servlet.doGet(request, response);
        response.getWriter().flush();
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String output = responseWriter.toString();
        assertTrue(output.contains("Invalid loan ID format"));
    }

    @Test
    void testDoGet_WithId_NotFound() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/5");
        when(loanServiceMock.getLoanById(5)).thenReturn(null);
        servlet.doGet(request, response);
        response.getWriter().flush();
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Loan not found"));
    }

    @Test
    void testDoGet_NoId_ReturnsAllLoans() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn(null);
        List<LoanDTO> loans = Arrays.asList(new LoanDTO(), new LoanDTO());
        when(loanServiceMock.getAllLoans()).thenReturn(loans);
        servlet.doGet(request, response);
        response.getWriter().flush();
        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.trim().startsWith("["));
    }

    @Test
    void testDoPost_Success() throws ServletException, IOException {
        String jsonInput = "{\"bookId\":10,\"readerId\":20,\"loanDate\":\"2025-02-23\",\"returnDate\":\"2025-03-01\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        LoanDTO createdDto = new LoanDTO();
        createdDto.setLoanId(1);
        createdDto.setBookId(10);
        createdDto.setReaderId(20);
        createdDto.setLoanDate("2025-02-23");
        createdDto.setReturnDate("2025-03-01");

        when(loanServiceMock.createLoan(any(LoanDTO.class))).thenReturn(createdDto);

        servlet.doPost(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"loanId\":1"));
    }

    @Test
    void testDoPut_Success() throws ServletException, IOException {
        String jsonInput = "{\"loanId\":1,\"bookId\":10,\"readerId\":20,\"loanDate\":\"2025-02-23\",\"returnDate\":\"2025-03-01\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);
        when(loanServiceMock.updateLoan(eq(1), any(LoanDTO.class))).thenReturn(true);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("\"loanId\":1"));
    }

    @Test
    void testDoPut_NotFound() throws ServletException, IOException {
        String jsonInput = "{\"loanId\":999,\"bookId\":10,\"readerId\":20,\"loanDate\":\"2025-02-23\",\"returnDate\":\"2025-03-01\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);
        when(loanServiceMock.updateLoan(eq(999), any(LoanDTO.class))).thenReturn(false);

        servlet.doPut(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Loan not found"));
    }

    @Test
    void testDoDelete_Success() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/1");
        when(loanServiceMock.deleteLoan(1)).thenReturn(true);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_OK);
        String output = responseWriter.toString();
        assertTrue(output.contains("Loan deleted"));
    }

    @Test
    void testDoDelete_InvalidId() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/abc");
        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String output = responseWriter.toString();
        assertTrue(output.contains("Invalid loan ID format"));
    }

    @Test
    void testDoDelete_NotFound() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/99");
        when(loanServiceMock.deleteLoan(99)).thenReturn(false);

        servlet.doDelete(request, response);
        response.getWriter().flush();

        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        String output = responseWriter.toString();
        assertTrue(output.contains("Loan not found"));
    }
}
