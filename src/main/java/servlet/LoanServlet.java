package servlet;

import dto.LoanDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mapper.LoanMapper;
import repository.LoanRepository;
import service.LoanService;
import util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/v1/loans/*")
public class LoanServlet extends HttpServlet {
    private final LoanService loanService = new LoanService(new LoanRepository(), new LoanMapper());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            List<LoanDTO> loans = loanService.getAllLoans();
            JsonUtil.sendJsonResponse(resp, loans, HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                LoanDTO loan = loanService.getLoanById(id);
                JsonUtil.checkExists(loan, "Loan not found", HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendJsonResponse(resp, loan, HttpServletResponse.SC_OK);
            } catch (NumberFormatException e) {
                JsonUtil.sendErrorResponse(resp, "Invalid loan ID format", HttpServletResponse.SC_BAD_REQUEST);
            } catch (JsonUtil.NotFoundException e) {
                JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoanDTO loanDTO = JsonUtil.parseJsonRequest(req, LoanDTO.class);
        try {
            JsonUtil.checkDto(loanDTO);
            LoanDTO createdLoan = loanService.createLoan(loanDTO);
            JsonUtil.sendJsonResponse(resp, createdLoan, HttpServletResponse.SC_CREATED);
        } catch (JsonUtil.InvalidDtoException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoanDTO loanDTO = JsonUtil.parseJsonRequest(req, LoanDTO.class);
        try {
            JsonUtil.checkDto(loanDTO);
            boolean updated = loanService.updateLoan(loanDTO.getLoanId(), loanDTO);
            if (updated) {
                JsonUtil.sendJsonResponse(resp, loanDTO, HttpServletResponse.SC_OK);
            } else {
                throw new JsonUtil.NotFoundException("Loan not found");
            }
        } catch (JsonUtil.InvalidDtoException | JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendErrorResponse(resp, "Missing loan ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = loanService.deleteLoan(id);
            JsonUtil.checkDeletion(deleted, "Loan not found", HttpServletResponse.SC_NOT_FOUND);
            JsonUtil.sendJsonResponse(resp, "Loan deleted", HttpServletResponse.SC_OK);
        } catch (NumberFormatException e) {
            JsonUtil.sendErrorResponse(resp, "Invalid loan ID format", HttpServletResponse.SC_BAD_REQUEST);
        } catch (JsonUtil.NotFoundException e) {
            JsonUtil.sendErrorResponse(resp, e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }
}