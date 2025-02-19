package servlet;

import dto.LoanDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.LoanService;
import util.JsonUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/loans/*")
public class LoanServlet extends HttpServlet {
    private final LoanService loanService = new LoanService();

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
                if (loan != null) {
                    JsonUtil.sendJsonResponse(resp, loan, HttpServletResponse.SC_OK);
                } else {
                    JsonUtil.sendJsonResponse(resp, "Loan not found", HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                JsonUtil.sendJsonResponse(resp, "Invalid loan ID format", HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoanDTO loanDTO = JsonUtil.parseJsonRequest(req, LoanDTO.class);
        if (loanDTO == null) {
            JsonUtil.sendJsonResponse(resp, "Invalid JSON", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        LoanDTO createdLoan = loanService.createLoan(loanDTO);
        JsonUtil.sendJsonResponse(resp, createdLoan, HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LoanDTO loanDTO = JsonUtil.parseJsonRequest(req, LoanDTO.class);
        if (loanDTO == null || loanDTO.getLoan_id() == null) {
            JsonUtil.sendJsonResponse(resp, "Invalid JSON format or missing loan ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        boolean updated = loanService.updateLoan(loanDTO.getLoan_id(), loanDTO);
        if (updated) {
            JsonUtil.sendJsonResponse(resp, loanDTO, HttpServletResponse.SC_OK);
        } else {
            JsonUtil.sendJsonResponse(resp, "Loan not found", HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo.equals("/") || pathInfo == null) {
            JsonUtil.sendJsonResponse(resp, "Missing loan ID", HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            boolean deleted = loanService.deleteLoan(id);
            if (deleted) {
                JsonUtil.sendJsonResponse(resp, "Loan deleted", HttpServletResponse.SC_OK);
            } else {
                JsonUtil.sendJsonResponse(resp, "Loan not found", HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (NumberFormatException e) {
            JsonUtil.sendJsonResponse(resp, "Invalid loan ID format", HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
