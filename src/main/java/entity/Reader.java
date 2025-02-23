package entity;

import lombok.Data;

@Data
public class Reader {
    private Integer readerId;
    private String name;
    private String email;
    private String registrationDate;
}