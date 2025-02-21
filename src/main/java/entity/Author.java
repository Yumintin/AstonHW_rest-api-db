package entity;

import lombok.Data;

@Data
public class Author {
    private Integer authorId;
    private String name;
    private Integer birthYear;
}