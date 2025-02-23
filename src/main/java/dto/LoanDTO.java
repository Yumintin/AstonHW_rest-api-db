package dto;

import lombok.Data;

import java.util.Objects;

@Data
public class LoanDTO {
    private Integer loanId;
    private Integer bookId;
    private Integer readerId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LoanDTO loanDTO = (LoanDTO) o;
        return Objects.equals(loanId, loanDTO.loanId) && Objects.equals(bookId, loanDTO.bookId) && Objects.equals(readerId, loanDTO.readerId) && Objects.equals(loanDate, loanDTO.loanDate) && Objects.equals(returnDate, loanDTO.returnDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId, bookId, readerId, loanDate, returnDate);
    }

    private String loanDate;
    private String returnDate;
}