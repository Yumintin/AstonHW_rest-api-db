package entity;

import lombok.Data;

import java.util.Date;

@Data
public class Loan {
    private Integer loan_id;
    private Integer book_id;
    private Integer reader_id;
    private String loan_date;
    private String return_date;
}