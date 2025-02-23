package entity;

import lombok.Data;

@Data
public class Loan {
    private Integer loanId;
    private Integer bookId;
    private Integer readerId;
    private String loanDate;
    private String returnDate;
}