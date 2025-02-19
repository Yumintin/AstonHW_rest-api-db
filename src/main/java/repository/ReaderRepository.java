package repository;

import entity.Reader;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReaderRepository {
    //CREATE
    public Reader create(Reader reader) {
        String sql = "INSERT INTO readers(name, email) VALUES(?,?) RETURNING reader_id, registration_date";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> fillPreparedStatementForReader(preparedStatement, reader, false),
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            reader.setReader_id(resultSet.getInt("reader_id"));

                            // Получаем временную метку из базы данных
                            Timestamp timestamp = resultSet.getTimestamp("registration_date");

                            // Если временная метка не null, конвертируем в строку в нужном формате
                            if (timestamp != null) {
                                // Форматируем дату в строку
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                reader.setRegistration_date(sdf.format(timestamp));
                            } else {
                                reader.setRegistration_date(null);
                            }
                        }
                    }
                    return reader;
                }
        );
    }

    //READ
    public Reader getById(int id) {
        String sql = "SELECT * FROM readers WHERE reader_id = ?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> preparedStatement.setInt(1, id),
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return resultSet.next() ? mapResultSetToReader(resultSet) : null;
                    }
                }
        );
    }

    //UPDATE
    public boolean update(Reader reader) {
        String sql = "UPDATE readers SET name = ?, email = ? WHERE reader_id = ?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> fillPreparedStatementForReader(preparedStatement, reader, true),
                preparedStatement -> preparedStatement.executeUpdate() > 0
        );
    }

    //DELETE
    public boolean delete(int id) {
        String sql = "DELETE FROM readers WHERE reader_id = ?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> preparedStatement.setInt(1, id),
                preparedStatement -> preparedStatement.executeUpdate() > 0
        );
    }

    public List<Reader> getAll() {
        String sql = "SELECT reader_id,name,email, registration_date FROM readers";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> {
                },
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        List<Reader> readers = new ArrayList<>();
                        while (resultSet.next()) {
                            readers.add(mapResultSetToReader(resultSet));
                        }
                        return readers;
                    }
                }
        );
    }

    // Заполняем PreparedStatement (insert, update)
    private void fillPreparedStatementForReader(PreparedStatement stmt, Reader reader, boolean isUpdate) throws SQLException {
        stmt.setString(1, reader.getName());
        stmt.setString(2, reader.getEmail());

        if (isUpdate) {
            if (reader.getReader_id() == null) {
                throw new IllegalArgumentException("Reader ID cannot be null for update");
            }
            stmt.setInt(3, reader.getReader_id());
        }
    }

    // Маппим ResultSet -> Reader
    private Reader mapResultSetToReader(ResultSet rs) throws SQLException {
        Reader reader = new Reader();
        reader.setReader_id(rs.getInt("reader_id"));
        reader.setName(rs.getString("name"));
        reader.setEmail(rs.getString("email"));
        // Преобразуем Timestamp в java.sql.Date
        Timestamp timestamp = rs.getTimestamp("registration_date");
        if (timestamp != null) {
            Date registrationDate = new Date(timestamp.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            reader.setRegistration_date(sdf.format(registrationDate));  // Форматируем в строку
        } else {
            reader.setRegistration_date(null);
        }


        return reader;
    }
}
