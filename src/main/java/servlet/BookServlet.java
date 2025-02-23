package servlet;

import dto.BookDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.BookMapper;
import repository.BookRepository;
import service.BookService;
import util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/books/*")
public class BookServlet extends HttpServlet {
    private final BookService bookService = new BookService(new BookRepository(),new BookMapper());

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
                JsonUtil.checkExists(book, "Book not found", HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendJsonResponse(resp, book, HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid book ID format", HttpServletResponse.SC_BAD_REQUEST);
            } catch (JsonUtil.NotFoundException e) {
                JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookDTO bookDto = JsonUtil.parseJsonRequest(req, BookDTO.class);
        try {
            JsonUtil.checkDto(bookDto);
            BookDTO createdBook = bookService.createBook(bookDto);
            JsonUtil.sendJsonResponse(resp, createdBook, HttpServletResponse.SC_CREATED);
        } catch (JsonUtil.InvalidDtoException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BookDTO bookDto = JsonUtil.parseJsonRequest(req, BookDTO.class);
        try {
            JsonUtil.checkDto(bookDto);
            boolean updated = bookService.updateBook(bookDto.getBookId(), bookDto);
            if (updated) {
                JsonUtil.sendJsonResponse(resp, bookDto, HttpServletResponse.SC_OK);
            } else {
                throw new JsonUtil.NotFoundException("Book not found");
            }
        } catch (JsonUtil.InvalidDtoException | JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendErrorResponse(resp, "Missing book ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = bookService.deleteBook(id);
            JsonUtil.checkDeletion(deleted, "Book not found", HttpServletResponse.SC_NOT_FOUND);
            JsonUtil.sendJsonResponse(resp, "Book deleted", HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, "Invalid book ID format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }
}