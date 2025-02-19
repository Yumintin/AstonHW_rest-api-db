package servlet;

import dto.ReaderDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.ReaderService;
import util.JsonUtil;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/readers/*")
public class ReaderServlet extends HttpServlet {
    private final ReaderService readerService = new ReaderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String pathInfo = req.getPathInfo();
        if(pathInfo==null || pathInfo.equals("/")){
            List<ReaderDTO> readers=readerService.getAllReaders();
            JsonUtil.sendJsonResponse(resp,readers,HttpServletResponse.SC_OK);
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                ReaderDTO reader = readerService.getReaderById(id);
                if(reader!=null){
                    JsonUtil.sendJsonResponse(resp,reader,HttpServletResponse.SC_OK);
                } else {
                    JsonUtil.sendJsonResponse(resp,"Reader not found",HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                JsonUtil.sendJsonResponse(resp,"Invalid reader ID format",HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ReaderDTO readerDTO=JsonUtil.parseJsonRequest(req,ReaderDTO.class);
        if(readerDTO==null){
            JsonUtil.sendJsonResponse(resp,"Invalid JSON",HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        ReaderDTO createdReader=readerService.createReader(readerDTO);
        JsonUtil.sendJsonResponse(resp,createdReader,HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ReaderDTO readerDTO=JsonUtil.parseJsonRequest(req,ReaderDTO.class);
        if(readerDTO==null || readerDTO.getReader_id()==null){
            JsonUtil.sendErrorResponse(resp,"Invalid JSON format or missing reader ID",HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        boolean updated = readerService.updateReader(readerDTO.getReader_id(),readerDTO);
        if(updated){
            JsonUtil.sendJsonResponse(resp,readerDTO,HttpServletResponse.SC_OK);
        } else {
            JsonUtil.sendErrorResponse(resp,"Reader not found",HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String pathInfo = req.getPathInfo();
        if(pathInfo==null || pathInfo.equals("/")){
            JsonUtil.sendErrorResponse(resp,"Missing reader ID",HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        try {
            int id = Integer.parseInt(pathInfo.substring(1));
           boolean deleted= readerService.deleteReader(id);
           if(deleted){
            JsonUtil.sendJsonResponse(resp,"Reader deleted",HttpServletResponse.SC_OK);
        } else {
            JsonUtil.sendErrorResponse(resp,"Reader not found",HttpServletResponse.SC_NOT_FOUND);
        }
    }catch (NumberFormatException e) {
        JsonUtil.sendErrorResponse(resp,"Invalid reader ID format",HttpServletResponse.SC_BAD_REQUEST);}
    }
}
