package dto;

import lombok.Data;

import java.util.Date;

@Data
public class LoanDTO {
    private Integer loan_id;
    private Integer book_id;
    private Integer reader_id;
    private String loan_date;
    private String return_date;
}