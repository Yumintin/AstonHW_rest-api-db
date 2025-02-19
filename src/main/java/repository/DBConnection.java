package repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Не удалось загрузить драйвер PostgreSQL", e);
        }
    }
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static <T> T executeWithConnection(SQLFunction<Connection, T> function) {
        try (Connection connection = getConnection()) {
            return function.apply(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при выполнении запроса к БД", e);
        }
    }

    public static <R> R executePreparedStatement(String sql, SQLConsumer<PreparedStatement> parameterSetter,
                                                 SQLFunction<PreparedStatement, R> executor) {
        return executeWithConnection(connection -> {
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                parameterSetter.accept(preparedStatement);
                return executor.apply(preparedStatement);
            }
        });
    }

}
