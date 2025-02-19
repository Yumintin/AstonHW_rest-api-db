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

    //CREATE
    public Loan create(Loan loan) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sql = "INSERT INTO loans(book_id, reader_id, loan_date, return_date) VALUES(?, ?, ?, ?) RETURNING loan_id, loan_date, return_date";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> {
                    preparedStatement.setInt(1, loan.getBook_id());
                    preparedStatement.setInt(2, loan.getReader_id());

                    // Преобразуем строки в объект Date
                    if (loan.getLoan_date() != null) {

                        try {
                            Date loanDate = sdf.parse(loan.getLoan_date());
                            preparedStatement.setDate(3, new java.sql.Date(loanDate.getTime()));
                        } catch (ParseException e) {
                            // Обрабатываем исключение, если формат даты неверен
                            throw new IllegalArgumentException("Invalid loan_date format");
                        }
                    } else {
                        preparedStatement.setDate(3, null);
                    }

                    if (loan.getReturn_date() != null) {
                        try {
                            Date returnDate = sdf.parse(loan.getReturn_date());
                            preparedStatement.setDate(4, new java.sql.Date(returnDate.getTime()));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException("Invalid return_date format");
                        }
                    } else {
                        preparedStatement.setDate(4, null);
                    }
                },
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            loan.setLoan_id(resultSet.getInt("loan_id"));
                            // Преобразование временной метки в строку
                            Timestamp loanTimestamp = resultSet.getTimestamp("loan_date");
                            if (loanTimestamp != null) {

                                loan.setLoan_date(sdf.format(loanTimestamp)); // Преобразуем дату в строку
                            }
                            Timestamp returnTimestamp = resultSet.getTimestamp("return_date");
                            if (returnTimestamp != null) {
                                loan.setReturn_date(sdf.format(returnTimestamp)); // Преобразуем дату в строку
                            }
                        }
                    }
                    return loan;
                }
        );
    }

    //READ
    public Loan getById(int id) {
        String sql = "SELECT * FROM loans WHERE loan_id=?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> preparedStatement.setInt(1, id),
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return resultSet.next() ? mapResultSetToLoan(resultSet) : null;
                    }
                }
        );
    }

    //UPDATE
    public boolean update(Loan loan) {
        String sql = "UPDATE loans SET book_id=?, reader_id=?, loan_date=?, return_date=? WHERE loan_id=?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> fillPreparedStatementForLoan(preparedStatement, loan, true),
                preparedStatement -> preparedStatement.executeUpdate() > 0
        );
    }

    //DELETE
    public boolean delete(int id) {
        String sql = "DELETE FROM loans WHERE loan_id=?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> preparedStatement.setInt(1, id),
                preparedStatement -> preparedStatement.executeUpdate() > 0
        );
    }

    //GET ALL
    public List<Loan> getAll() {
        String sql = "SELECT loan_id,book_id,reader_id,loan_date,return_date FROM loans";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> {
                },
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        List<Loan> loans = new ArrayList<>();
                        while (resultSet.next()) {
                            loans.add(mapResultSetToLoan(resultSet));
                        }
                        return loans;
                    }
                }
        );
    }

    // Заполняем PreparedStatement (insert, update)
    private void fillPreparedStatementForLoan(PreparedStatement stmt, Loan loan, boolean isUpdate) throws SQLException {
        try {
            stmt.setInt(1, loan.getBook_id());
            stmt.setInt(2, loan.getReader_id());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // Преобразование строк в дату
            if (loan.getLoan_date() != null) {

                Date loanDate = sdf.parse(loan.getLoan_date());
                stmt.setDate(3, new java.sql.Date(loanDate.getTime()));
            } else {
                stmt.setDate(3, null);  // Если даты нет
            }

            if (loan.getReturn_date() != null) {
                Date returnDate = sdf.parse(loan.getReturn_date());
                stmt.setDate(4, new java.sql.Date(returnDate.getTime()));
            } else {
                stmt.setDate(4, null);  // Если даты нет
            }

            if (isUpdate) {
                stmt.setInt(5, loan.getLoan_id());
            }
        } catch (ParseException e) {
            throw new SQLException("Invalid date format", e);  // Ловим ошибку, если формат даты неверный
        }
    }

    // Маппим ResultSet -> Loan
    private Loan mapResultSetToLoan(ResultSet rs) throws SQLException {
        Loan loan = new Loan();
        loan.setLoan_id(rs.getInt("loan_id"));
        loan.setBook_id(rs.getInt("book_id"));
        loan.setReader_id(rs.getInt("reader_id"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Timestamp loanTimestamp = rs.getTimestamp("loan_date");
        if (loanTimestamp != null) {

            loan.setLoan_date(sdf.format(loanTimestamp));  // Форматируем в строку
        }

        Timestamp returnTimestamp = rs.getTimestamp("return_date");
        if (returnTimestamp != null) {
            loan.setReturn_date(sdf.format(returnTimestamp));  // Форматируем в строку
        }

        return loan;
    }
}
