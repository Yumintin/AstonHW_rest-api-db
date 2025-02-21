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

    private static final String SQL_INSERT = "INSERT INTO readers(name, email) VALUES(?,?) RETURNING reader_id, registration_date";
    private static final String SQL_SELECT_BY_ID = "SELECT reader_id, name, email, registration_date FROM readers WHERE reader_id = ?";
    private static final String SQL_UPDATE = "UPDATE readers SET name = ?, email = ? WHERE reader_id = ?";
    private static final String SQL_DELETE = "DELETE FROM readers WHERE reader_id = ?";
    private static final String SQL_SELECT_ALL = "SELECT reader_id, name, email, registration_date FROM readers";

    /**
     * Создает новую запись Reader в базе данных.
     *
     * @param reader объект Reader для создания
     * @return созданный объект Reader с заполненным readerId и registrationDate
     */
    public Reader create(Reader reader) {
        return DBConnection.executePreparedStatement(SQL_INSERT,
                stmt -> fillPreparedStatementForReader(stmt, reader, false),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            reader.setReaderId(rs.getInt("reader_id"));
                            reader.setRegistrationDate(formatTimestamp(rs.getTimestamp("registration_date")));
                        }
                    }
                    return reader;
                });
    }

    /**
     * Находит запись Reader в базе данных по ID.
     *
     * @param id идентификатор читателя
     * @return объект Reader, если запись найдена, иначе null
     */
    public Reader getById(int id) {
        return DBConnection.executePreparedStatement(SQL_SELECT_BY_ID,
                stmt -> stmt.setInt(1, id),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next() ? mapResultSetToReader(rs) : null;
                    }
                });
    }

    /**
     * Обновляет существующую запись Reader.
     *
     * @param reader объект Reader с обновленными данными
     * @return true, если обновление прошло успешно, иначе false
     */
    public boolean update(Reader reader) {
        return DBConnection.executePreparedStatement(SQL_UPDATE,
                stmt -> fillPreparedStatementForReader(stmt, reader, true),
                stmt -> stmt.executeUpdate() > 0
        );
    }

    /**
     * Удаляет запись Reader по заданному идентификатору.
     *
     * @param id идентификатор читателя
     * @return true, если удаление прошло успешно, иначе false
     */
    public boolean delete(int id) {
        return DBConnection.executePreparedStatement(SQL_DELETE,
                stmt -> stmt.setInt(1, id),
                stmt -> stmt.executeUpdate() > 0);
    }

    /**
     * Возвращает список всех записей Reader из базы данных.
     *
     * @return список всех объектов Reader
     */
    public List<Reader> getAll() {
        return DBConnection.executePreparedStatement(SQL_SELECT_ALL,
                stmt -> {
                },
                stmt -> {
                    List<Reader> readers = new ArrayList<>();
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            readers.add(mapResultSetToReader(rs));
                        }
                    }
                    return readers;
                });
    }

    /**
     * Заполняет объект PreparedStatement значениями из объекта Reader.
     *
     * @param stmt   PreparedStatement, который нужно заполнить
     * @param reader объект Reader, содержащий данные
     * @param isUpdate true, если операция обновления, false для операции вставки
     * @throws SQLException в случае ошибки доступа к базе данных
     */
    private void fillPreparedStatementForReader(PreparedStatement stmt, Reader reader, boolean isUpdate) throws SQLException {
        stmt.setString(1, reader.getName());
        stmt.setString(2, reader.getEmail());
        if (isUpdate) {
            stmt.setInt(3, reader.getReaderId());
        }
    }

    /**
     * Преобразует текущую строку объекта ResultSet в объект Reader.
     *
     * @param rs объект ResultSet, уже установленный на нужную строку
     * @return объект Reader, полученный из данных ResultSet
     * @throws SQLException в случае ошибки доступа к базе данных
     */
    private Reader mapResultSetToReader(ResultSet rs) throws SQLException {
        Reader reader = new Reader();
        reader.setReaderId(rs.getInt("reader_id"));
        reader.setName(rs.getString("name"));
        reader.setEmail(rs.getString("email"));
        reader.setRegistrationDate(formatTimestamp(rs.getTimestamp("registration_date")));
        return reader;
    }

    /**
     * Форматирует Timestamp в строку (yyyy-MM-dd).
     *
     * @param timestamp объект Timestamp
     * @return строка с датой в формате yyyy-MM-dd, либо null, если timestamp равен null
     */
    private String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(timestamp.getTime()));
    }
}
