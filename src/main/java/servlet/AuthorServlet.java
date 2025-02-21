package servlet;

import dto.AuthorDTO;
import service.AuthorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/authors/*")
public class AuthorServlet extends HttpServlet {
    private final AuthorService authorService = new AuthorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                AuthorDTO authorDTO = authorService.getAuthorById(id);
                JsonUtil.checkExists(authorDTO, "Author not found", HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendJsonResponse(resp, authorDTO, HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
            } catch (JsonUtil.NotFoundException e) {
                JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            List<AuthorDTO> authors = authorService.getAllAuthors();
            JsonUtil.sendJsonResponse(resp, authors, HttpServletResponse.SC_OK);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthorDTO authorDto = JsonUtil.parseJsonRequest(req, AuthorDTO.class);
        try {
            JsonUtil.checkDto(authorDto);
            AuthorDTO createdAuthor = authorService.createAuthor(authorDto);
            JsonUtil.sendJsonResponse(resp, createdAuthor, HttpServletResponse.SC_CREATED);
        } catch (JsonUtil.InvalidDtoException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthorDTO authorDto = JsonUtil.parseJsonRequest(req, AuthorDTO.class);
        try {
            JsonUtil.checkDto(authorDto);
            boolean updated = authorService.updateAuthor(authorDto.getAuthorId(), authorDto);
            if (updated) {
                JsonUtil.sendJsonResponse(resp, authorDto, HttpServletResponse.SC_OK);
            } else {
                throw new JsonUtil.NotFoundException("Author not found");
            }
        } catch (JsonUtil.InvalidDtoException | JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        String[] pathParts = path.split("/");
        if (pathParts.length > 0) {
            try {
                int id = Integer.parseInt(pathParts[pathParts.length - 1]);
                boolean deleted = authorService.deleteAuthor(id);
                JsonUtil.checkDeletion(deleted, "Author not found", HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendJsonResponse(resp, "Author deleted", HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
            } catch (JsonUtil.NotFoundException e) {
                JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            JsonUtil.sendErrorResponse(resp, "Missing ID parameter", HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}