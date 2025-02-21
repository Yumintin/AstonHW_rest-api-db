package mapper;

import dto.LoanDTO;
import entity.Loan;

public class LoanMapper implements Mapper<Loan, LoanDTO> {
@Override
    public LoanDTO toDto(Loan loan) {
        if (loan == null) {
            return null;
        }
        LoanDTO dto = new LoanDTO();
        dto.setLoanId(loan.getLoanId());
        dto.setBookId(loan.getBookId());
        dto.setReaderId(loan.getReaderId());
        dto.setLoanDate(loan.getLoanDate());
        dto.setReturnDate(loan.getReturnDate());
        return dto;
    }
@Override
    public Loan toEntity(LoanDTO dto) {
        if (dto == null) {
            return null;
        }
        Loan loan = new Loan();
        loan.setLoanId(dto.getLoanId());
        loan.setBookId(dto.getBookId());
        loan.setReaderId(dto.getReaderId());
        loan.setLoanDate(dto.getLoanDate());
        loan.setReturnDate(dto.getReturnDate());
        return loan;
    }
}
