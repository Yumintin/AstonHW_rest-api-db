package servlet;

import dto.BookDTO;
import entity.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.BookService;
import util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/books/*")
public class BookServlet extends HttpServlet {
    private final BookService bookService = new BookService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<BookDTO> books = bookService.getAllBooks();
            JsonUtil.sendJsonResponse(resp, books, HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                BookDTO book = bookService.getBookById(id);
                if (book != null) {
                    JsonUtil.sendJsonResponse(resp, book, HttpServletResponse.SC_OK);
                } else {
                    JsonUtil.sendErrorResponse(resp, "Book not found", HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid book ID format", HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookDTO bookDto = JsonUtil.parseJsonRequest(req, BookDTO.class);
        if (bookDto == null) {
            JsonUtil.sendErrorResponse(resp, "Invalid JSON", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        BookDTO createdBook = bookService.createBook(bookDto);
        JsonUtil.sendJsonResponse(resp, createdBook, HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookDTO bookDto = JsonUtil.parseJsonRequest(req, BookDTO.class);
        if (bookDto == null || bookDto.getBook_id() == null) {
            JsonUtil.sendErrorResponse(resp, "Invalid JSON format or missing book ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        boolean updated = bookService.updateBook(bookDto.getBook_id(), bookDto);
        if (updated) {
            JsonUtil.sendJsonResponse(resp, bookDto, HttpServletResponse.SC_OK);
        } else {
            JsonUtil.sendErrorResponse(resp, "Book not found", HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendErrorResponse(resp, "Missing book ID", HttpServletResponse.SC_BAD_REQUEST);
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = bookService.deleteBook(id);
            if (deleted) {
                JsonUtil.sendJsonResponse(resp, "Book deleted", HttpServletResponse.SC_OK);
            } else {
                JsonUtil.sendErrorResponse(resp, "Book not found", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, "Invalid book ID format", HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
