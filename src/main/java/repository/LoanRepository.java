package repository;

import entity.Loan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoanRepository {

    private static final String SQL_INSERT = "INSERT INTO loans(book_id, reader_id, loan_date, return_date) VALUES(?, ?, ?, ?) RETURNING loan_id, loan_date, return_date";
    private static final String SQL_SELECT_BY_ID = "SELECT loan_id, book_id, reader_id, loan_date, return_date FROM loans WHERE loan_id = ?";
    private static final String SQL_UPDATE = "UPDATE loans SET book_id = ?, reader_id = ?, loan_date = ?, return_date = ? WHERE loan_id = ?";
    private static final String SQL_DELETE = "DELETE FROM loans WHERE loan_id = ?";
    private static final String SQL_SELECT_ALL = "SELECT loan_id, book_id, reader_id, loan_date, return_date FROM loans";

    /**
     * Создает новую запись Loan в базе данных.
     *
     * @param loan объект Loan для создания
     * @return созданный объект Loan с заполненным loanId и датами
     */
    public Loan create(Loan loan) {
        return DBConnection.executePreparedStatement(SQL_INSERT,
                stmt -> fillPreparedStatementForLoan(stmt, loan, false),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            loan.setLoanId(rs.getInt("loan_id"));
                            loan.setLoanDate(formatTimestamp(rs.getTimestamp("loan_date")));
                            loan.setReturnDate(formatTimestamp(rs.getTimestamp("return_date")));
                        }
                    }
                    return loan;
                });
    }

    /**
     * Находит запись Loan в базе данных по ID.
     *
     * @param id идентификатор займа
     * @return объект Loan, если запись найдена, иначе null
     */
    public Loan getById(int id) {
        return DBConnection.executePreparedStatement(SQL_SELECT_BY_ID,
                stmt -> stmt.setInt(1, id),
                stmt -> {
                    try (ResultSet rs = stmt.executeQuery()) {
                        return rs.next() ? mapResultSetToLoan(rs) : null;
                    }
                });
    }

    /**
     * Обновляет существующую запись Loan.
     *
     * @param loan объект Loan с обновленными данными
     * @return true, если обновление прошло успешно, иначе false
     */
    public boolean update(Loan loan) {
        return DBConnection.executePreparedStatement(SQL_UPDATE,
                stmt -> fillPreparedStatementForLoan(stmt, loan, true),
                stmt -> stmt.executeUpdate() > 0);
    }

    /**
     * Удаляет запись Loan по заданному идентификатору.
     *
     * @param id идентификатор займа
     * @return true, если удаление прошло успешно, иначе false
     */
    public boolean delete(int id) {
        return DBConnection.executePreparedStatement(SQL_DELETE,
                stmt -> stmt.setInt(1, id),
                stmt -> stmt.executeUpdate() > 0);
    }

    /**
     * Возвращает список всех записей Loan из базы данных.
     *
     * @return список всех объектов Loan
     */
    public List<Loan> getAll() {
        return DBConnection.executePreparedStatement(SQL_SELECT_ALL,
                stmt -> {
                },
                stmt -> {
                    List<Loan> loans = new ArrayList<>();
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            loans.add(mapResultSetToLoan(rs));
                        }
                    }
                    return loans;
                });
    }

    /**
     * Заполняет объект PreparedStatement значениями из объекта Loan.
     *
     * @param stmt     PreparedStatement, который нужно заполнить
     * @param loan     объект Loan, содержащий данные
     * @param isUpdate true, если операция обновления, false для операции вставки
     * @throws SQLException в случае ошибки доступа к базе данных
     */
    private void fillPreparedStatementForLoan(PreparedStatement stmt, Loan loan, boolean isUpdate) throws SQLException {
        stmt.setInt(1, loan.getBookId());
        stmt.setInt(2, loan.getReaderId());
        stmt.setDate(3, parseDate(loan.getLoanDate()));
        stmt.setDate(4, parseDate(loan.getReturnDate()));
        if (isUpdate) {
            stmt.setInt(5, loan.getLoanId());
        }
    }

    /**
     * Преобразует текущую строку объекта ResultSet в объект Loan.
     *
     * @param rs объект ResultSet, уже установленный на нужную строку
     * @return объект Loan, полученный из данных ResultSet
     * @throws SQLException в случае ошибки доступа к базе данных
     */
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanId(rs.getInt("loan_id"));
        loan.setBookId(rs.getInt("book_id"));
        loan.setReaderId(rs.getInt("reader_id"));
        loan.setLoanDate(formatTimestamp(rs.getTimestamp("loan_date")));
        loan.setReturnDate(formatTimestamp(rs.getTimestamp("return_date")));
        return loan;
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

    /**
     * Преобразует строку в SQL Date.
     *
     * @param date строка с датой (yyyy-MM-dd)
     * @return объект java.sql.Date или null, если входная строка пуста
     */
    private java.sql.Date parseDate(String date) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        try {
            return new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format: " + date);
        }
    }
}
