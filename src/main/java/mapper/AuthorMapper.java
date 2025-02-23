package mapper;

import dto.AuthorDTO;
import entity.Author;

public class AuthorMapper implements Mapper<Author, AuthorDTO> {
    @Override
    public AuthorDTO toDto(Author author) {
        if (author == null) {
            return null;
        }
        AuthorDTO dto = new AuthorDTO();
        dto.setAuthorId(author.getAuthorId());
        dto.setName(author.getName());
        dto.setBirthYear(author.getBirthYear());
        return dto;
    }

    @Override
    public Author toEntity(AuthorDTO dto) {
        if (dto == null) {
            return null;
        }
        Author author = new Author();
        author.setAuthorId(dto.getAuthorId());
        author.setName(dto.getName());
        author.setBirthYear(dto.getBirthYear());
        return author;
    }

}
