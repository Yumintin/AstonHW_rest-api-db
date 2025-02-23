package servlet;

import dto.ReaderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.ReaderMapper;
import repository.ReaderRepository;
import service.ReaderService;
import util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/readers/*")
public class ReaderServlet extends HttpServlet {
    private final ReaderService readerService = new ReaderService(new ReaderRepository(), new ReaderMapper());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<ReaderDTO> readers = readerService.getAllReaders();
            JsonUtil.sendJsonResponse(resp, readers, HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                ReaderDTO reader = readerService.getReaderById(id);
                JsonUtil.checkExists(reader, "Reader not found", HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendJsonResponse(resp, reader, HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid reader ID format", HttpServletResponse.SC_BAD_REQUEST);
            } catch (JsonUtil.NotFoundException e) {
                JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ReaderDTO readerDTO = JsonUtil.parseJsonRequest(req, ReaderDTO.class);
        try {
            JsonUtil.checkDto(readerDTO);
            ReaderDTO createdReader = readerService.createReader(readerDTO);
            JsonUtil.sendJsonResponse(resp, createdReader, HttpServletResponse.SC_CREATED);
        } catch (JsonUtil.InvalidDtoException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ReaderDTO readerDTO = JsonUtil.parseJsonRequest(req, ReaderDTO.class);
        try {
            JsonUtil.checkDto(readerDTO);
            boolean updated = readerService.updateReader(readerDTO.getReaderId(), readerDTO);
            if (updated) {
                JsonUtil.sendJsonResponse(resp, readerDTO, HttpServletResponse.SC_OK);
            } else {
                throw new JsonUtil.NotFoundException("Reader not found");
            }
        } catch (JsonUtil.InvalidDtoException | JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendErrorResponse(resp, "Missing reader ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = readerService.deleteReader(id);
            JsonUtil.checkDeletion(deleted, "Reader not found", HttpServletResponse.SC_NOT_FOUND);
            JsonUtil.sendJsonResponse(resp, "Reader deleted", HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, "Invalid reader ID format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }
}