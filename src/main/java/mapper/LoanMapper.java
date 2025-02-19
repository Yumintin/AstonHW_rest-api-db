package mapper;

import dto.LoanDTO;
import entity.Loan;

public class LoanMapper {
    public static LoanDTO toLoanDTO(Loan loan) {
        if (loan == null) return null;
        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setLoan_id(loan.getLoan_id());
        loanDTO.setBook_id(loan.getBook_id());
        loanDTO.setReader_id(loan.getReader_id());
        loanDTO.setLoan_date(loan.getLoan_date());
        loanDTO.setReturn_date(loan.getReturn_date());
        return loanDTO;
    }
    public static Loan toLoan(LoanDTO loanDTO) {
        if (loanDTO == null) return null;
        Loan loan = new Loan();
        loan.setLoan_id(loanDTO.getLoan_id());
        loan.setBook_id(loanDTO.getBook_id());
        loan.setReader_id(loanDTO.getReader_id());
        loan.setLoan_date(loanDTO.getLoan_date());
        loan.setReturn_date(loanDTO.getReturn_date());
        return loan;
    }
}
