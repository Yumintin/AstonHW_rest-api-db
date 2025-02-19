package dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReaderDTO {
    private Integer reader_id;
    private String name;
    private String email;
    private String registration_date;
}