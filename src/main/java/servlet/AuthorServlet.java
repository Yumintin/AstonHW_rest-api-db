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

@WebServlet("/authors/*")
public class AuthorServlet extends HttpServlet {
    private final AuthorService authorService = new AuthorService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null) {
            try {
                int id = Integer.parseInt(idParam);
                AuthorDTO authorDTO = authorService.getAuthorById(id);
                if (authorDTO != null) {
                    JsonUtil.sendJsonResponse(resp, authorDTO, HttpServletResponse.SC_OK);
                } else {
                    JsonUtil.sendErrorResponse(resp, "Author not found", HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            List<AuthorDTO> authors = authorService.getAllAuthors();
            JsonUtil.sendJsonResponse(resp, authors, HttpServletResponse.SC_OK);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthorDTO authorDto = JsonUtil.parseJsonRequest(req, AuthorDTO.class);
        if (authorDto == null) {
            JsonUtil.sendErrorResponse(resp, "Invalid JSON format", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        AuthorDTO createAuthor = authorService.createAuthor(authorDto);
        JsonUtil.sendJsonResponse(resp, createAuthor, HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AuthorDTO authorDto = JsonUtil.parseJsonRequest(req, AuthorDTO.class);
        if (authorDto == null || authorDto.getAuthor_id() == null) {
            System.out.println("Error: author_id is null");
            JsonUtil.sendErrorResponse(resp, "Invalid JSON format or missing ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        boolean updated = authorService.updateAuthor(authorDto.getAuthor_id(), authorDto);
        if (updated) {
            JsonUtil.sendJsonResponse(resp, authorDto, HttpServletResponse.SC_OK);
        } else {
            JsonUtil.sendErrorResponse(resp, "Author not found", HttpServletResponse.SC_NOT_FOUND);
        }
    }

    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI();
        String[] pathParts = path.split("/");
        if (pathParts.length > 0) {
            try {
                int id = Integer.parseInt(pathParts[pathParts.length - 1]);
                boolean deleted = authorService.deleteAuthor(id);
                if (deleted) {
                    JsonUtil.sendJsonResponse(resp, "Author deleted", HttpServletResponse.SC_OK);
                } else {
                    JsonUtil.sendErrorResponse(resp, "Author not found", HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid ID format", HttpServletResponse.SC_BAD_REQUEST);
            }
        } else {
            JsonUtil.sendErrorResponse(resp, "Missing ID parameter", HttpServletResponse.SC_BAD_REQUEST);
        }
    }


}
