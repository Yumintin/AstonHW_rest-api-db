package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJsonRequest(HttpServletRequest req, Class<T> clazz) {
        try (BufferedReader reader = req.getReader()) {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }
            return objectMapper.readValue(jsonString.toString(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toJson(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }

    public static void sendJsonResponse(HttpServletResponse resp, Object data, int status) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(status);
        resp.getWriter().write(toJson(data));
    }

    public static void sendErrorResponse(HttpServletResponse resp, String message, int status) throws IOException {
        sendJsonResponse(resp, new ErrorResponse(message), status);
    }

    public static void checkDto(Object dto) throws InvalidDtoException {
        if (dto == null) {
            throw new InvalidDtoException("Invalid JSON format or missing required fields");
        }
    }

    public static void checkExists(Object obj, String errorMessage, int status) throws NotFoundException {
        if (obj == null) {
            throw new NotFoundException(errorMessage);
        }
    }

    public static void checkDeletion(boolean deleted, String errorMessage, int status) throws NotFoundException {
        if (!deleted) {
            throw new NotFoundException(errorMessage);
        }
    }

    public static class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    public static class InvalidDtoException extends Exception {
        public InvalidDtoException(String message) {
            super(message);
        }
    }

    public static class NotFoundException extends Exception {
        public NotFoundException(String message) {
            super(message);
        }
    }
}