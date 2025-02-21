package dto;

import lombok.Data;

import java.util.Objects;

@Data
public class AuthorDTO {
    private Integer authorId;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDTO authorDTO = (AuthorDTO) o;
        return Objects.equals(authorId, authorDTO.authorId) && Objects.equals(name, authorDTO.name) && Objects.equals(birthYear, authorDTO.birthYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, name, birthYear);
    }

    private Integer birthYear;
}