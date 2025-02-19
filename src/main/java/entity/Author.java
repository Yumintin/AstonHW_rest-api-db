package entity;

import lombok.Data;

@Data
public class Author {
    private Integer author_id;
    private String name;
    private Integer birth_year;
}