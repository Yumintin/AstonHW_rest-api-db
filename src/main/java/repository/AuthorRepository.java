package repository;

import entity.Author;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorRepository {

    //CREATE
    public Author create(Author author) {
        String sql = "INSERT INTO authors(name, birth_year) values(?,?) RETURNING author_id";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> fillPreparedStatementForAuthor(preparedStatement, author, false),
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        if (resultSet.next()) {
                            author.setAuthor_id(resultSet.getInt("author_id"));
                        }
                    }
                    return author;
                }
        );
    }

    //READ
    public Author findbyId(int id) {
        String sql = "SELECT * FROM authors WHERE author_id = ?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> preparedStatement.setInt(1, id),
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return resultSet.next() ? mapResultSetToAuthor(resultSet) : null;
                    }
                }
        );
    }

    //UPDATE
    public boolean update(Author author) {
        String sql = "UPDATE authors SET name = ?, birth_year = ? WHERE author_id = ?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> fillPreparedStatementForAuthor(preparedStatement, author, true),
                preparedStatement -> preparedStatement.executeUpdate() > 0
        );
    }

    //DELETE
    public boolean delete(int id) {
        String sql = "DELETE FROM authors WHERE author_id = ?";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> preparedStatement.setInt(1, id),
                preparedStatement -> preparedStatement.executeUpdate() > 0
        );
    }

    //READALL
    public List<Author> findAll() {
        String sql = "SELECT author_id,name,birth_year FROM authors";
        return DBConnection.executePreparedStatement(sql,
                preparedStatement -> {
                },
                preparedStatement -> {
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        List<Author> authors = new ArrayList<>();
                        while (resultSet.next()) {
                            authors.add(mapResultSetToAuthor(resultSet));
                        }
                        return authors;
                    }
                }
        );
    }

    // Метод для заполнения PreparedStatement (вставка и обновление)
    private void fillPreparedStatementForAuthor(PreparedStatement stmt, Author author, boolean isUpdate) throws SQLException {
        stmt.setString(1, author.getName());

        if (author.getBirth_year() != null) {
            stmt.setInt(2, author.getBirth_year());
        } else {
            stmt.setNull(2, Types.INTEGER);
        }

        if (isUpdate) {
            if (author.getAuthor_id() == null) {
                throw new IllegalArgumentException("Author ID cannot be null for update");
            }
            stmt.setInt(3, author.getAuthor_id());
        }
    }

    // Метод для маппинга ResultSet -> Author
    private Author mapResultSetToAuthor(ResultSet rs) throws SQLException {
        Author author = new Author();
        author.setAuthor_id(rs.getInt("author_id"));
        author.setName(rs.getString("name"));
        int birthYear = rs.getInt("birth_year");
        author.setBirth_year(rs.wasNull() ? null : birthYear);
        return author;
    }
}
