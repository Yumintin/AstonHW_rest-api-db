package mapper;

import dto.AuthorDTO;
import entity.Author;

public class AuthorMapper {

    public static AuthorDTO toAuthorDTO(Author author) {
        if (author == null) return null;
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setAuthor_id(author.getAuthor_id());
        authorDTO.setName(author.getName());
        authorDTO.setBirth_year(author.getBirth_year());
        return authorDTO;
    }

    public static Author toAuthor(AuthorDTO authorDTO) {
        if (authorDTO == null) return null;
        Author author = new Author();
        author.setAuthor_id(authorDTO.getAuthor_id());
        author.setName(authorDTO.getName());
        author.setBirth_year(authorDTO.getBirth_year());
        return author;
    }

}
