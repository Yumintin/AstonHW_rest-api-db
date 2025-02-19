package dto;

import lombok.Data;

@Data
public class AuthorDTO {
    private Integer author_id;
    private String name;
    private Integer birth_year;
}