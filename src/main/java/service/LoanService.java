package service;

import dto.LoanDTO;
import entity.Loan;
import mapper.LoanMapper;
import repository.LoanRepository;

import java.util.List;
import java.util.stream.Collectors;

public class LoanService {
    private final LoanRepository repository;
    private final LoanMapper loanMapper;

    public LoanService(LoanRepository repository, LoanMapper loanMapper) {
        this.repository=repository;
        this.loanMapper=loanMapper;
    }
    public LoanDTO createLoan(LoanDTO dto) {
        Loan loan= loanMapper.toEntity(dto);
        Loan created=repository.create(loan);
        return loanMapper.toDto(created);
    }

    public LoanDTO getLoanById(int id) {
        Loan loan=repository.getById(id);
        return loanMapper.toDto(loan);
    }

    public List<LoanDTO> getAllLoans() {
        List<Loan> loans=repository.getAll();
        return loans.stream().map(loanMapper::toDto).collect(Collectors.toList());
    }

    public boolean updateLoan(int id, LoanDTO dto) {
        Loan loan=loanMapper.toEntity(dto);
        loan.setLoanId(id);
        return repository.update(loan);
    }
    public boolean deleteLoan(int id) {
        return repository.delete(id);
    }
}
