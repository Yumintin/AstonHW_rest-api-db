package service;

import dto.LoanDTO;
import entity.Loan;
import mapper.LoanMapper;
import repository.LoanRepository;

import java.util.List;
import java.util.stream.Collectors;

public class LoanService {
    private final LoanRepository repository=new LoanRepository();

    public LoanDTO createLoan(LoanDTO dto) {
        Loan loan= LoanMapper.toLoan(dto);
        Loan created=repository.create(loan);
        return LoanMapper.toLoanDTO(created);
    }

    public LoanDTO getLoanById(int id) {
        Loan loan=repository.getById(id);
        return LoanMapper.toLoanDTO(loan);
    }

    public List<LoanDTO> getAllLoans() {
        List<Loan> loans=repository.getAll();
        return loans.stream().map(LoanMapper::toLoanDTO).collect(Collectors.toList());
    }

    public boolean updateLoan(int id, LoanDTO dto) {
        Loan loan=LoanMapper.toLoan(dto);
        loan.setLoan_id(id);
        return repository.update(loan);
    }
    public boolean deleteLoan(int id) {
        return repository.delete(id);
    }
}
