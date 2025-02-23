package service;

import dto.LoanDTO;
import entity.Loan;
import mapper.LoanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.LoanRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class LoanServiceTest {

    private LoanRepository repository;
    private LoanMapper loanMapper;
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        repository = mock(LoanRepository.class);
        loanMapper = mock(LoanMapper.class);
        loanService = new LoanService(repository, loanMapper);
    }

    @Test
    void testCreateLoan() {
        LoanDTO inputDto = new LoanDTO();
        inputDto.setBookId(1);
        inputDto.setReaderId(1);
        inputDto.setLoanDate("2023-01-01");
        inputDto.setReturnDate("2023-01-15");

        Loan loanEntity = new Loan();
        loanEntity.setBookId(1);
        loanEntity.setReaderId(1);
        loanEntity.setLoanDate("2023-01-01");
        loanEntity.setReturnDate("2023-01-15");

        Loan createdLoan = new Loan();
        createdLoan.setLoanId(1);
        createdLoan.setBookId(1);
        createdLoan.setReaderId(1);
        createdLoan.setLoanDate("2023-01-01");
        createdLoan.setReturnDate("2023-01-15");

        LoanDTO outputDto = new LoanDTO();
        outputDto.setLoanId(1);
        outputDto.setBookId(1);
        outputDto.setReaderId(1);
        outputDto.setLoanDate("2023-01-01");
        outputDto.setReturnDate("2023-01-15");

        when(loanMapper.toEntity(inputDto)).thenReturn(loanEntity);
        when(repository.create(loanEntity)).thenReturn(createdLoan);
        when(loanMapper.toDto(createdLoan)).thenReturn(outputDto);

        LoanDTO result = loanService.createLoan(inputDto);

        assertNotNull(result);
        assertEquals(1, result.getLoanId());
        assertEquals("2023-01-01", result.getLoanDate());

        verify(loanMapper).toEntity(inputDto);
        verify(repository).create(loanEntity);
        verify(loanMapper).toDto(createdLoan);
    }

    @Test
    void testGetLoanById() {
        int id = 1;
        Loan loanEntity = new Loan();
        loanEntity.setLoanId(id);
        loanEntity.setBookId(1);
        loanEntity.setReaderId(1);
        loanEntity.setLoanDate("2023-01-01");
        loanEntity.setReturnDate("2023-01-15");

        LoanDTO outputDto = new LoanDTO();
        outputDto.setLoanId(id);
        outputDto.setBookId(1);
        outputDto.setReaderId(1);
        outputDto.setLoanDate("2023-01-01");
        outputDto.setReturnDate("2023-01-15");

        when(repository.getById(id)).thenReturn(loanEntity);
        when(loanMapper.toDto(loanEntity)).thenReturn(outputDto);

        LoanDTO result = loanService.getLoanById(id);

        assertNotNull(result);
        assertEquals(id, result.getLoanId());

        verify(repository).getById(id);
        verify(loanMapper).toDto(loanEntity);
    }

    @Test
    void testGetAllLoans() {
        Loan loan1 = new Loan();
        loan1.setLoanId(1);
        loan1.setLoanDate("2023-01-01");

        Loan loan2 = new Loan();
        loan2.setLoanId(2);
        loan2.setLoanDate("2023-02-01");

        List<Loan> loans = Arrays.asList(loan1, loan2);

        LoanDTO dto1 = new LoanDTO();
        dto1.setLoanId(1);
        dto1.setLoanDate("2023-01-01");

        LoanDTO dto2 = new LoanDTO();
        dto2.setLoanId(2);
        dto2.setLoanDate("2023-02-01");

        when(repository.getAll()).thenReturn(loans);
        when(loanMapper.toDto(loan1)).thenReturn(dto1);
        when(loanMapper.toDto(loan2)).thenReturn(dto2);

        List<LoanDTO> result = loanService.getAllLoans();

        assertNotNull(result);
        assertEquals(2, result.size());

        verify(repository).getAll();
        verify(loanMapper).toDto(loan1);
        verify(loanMapper).toDto(loan2);
    }

    @Test
    void testUpdateLoan() {
        int id = 1;
        LoanDTO inputDto = new LoanDTO();
        inputDto.setBookId(1);
        inputDto.setReaderId(1);
        inputDto.setLoanDate("2023-03-01");
        inputDto.setReturnDate("2023-03-15");

        Loan loanEntity = new Loan();
        loanEntity.setBookId(1);
        loanEntity.setReaderId(1);
        loanEntity.setLoanDate("2023-03-01");
        loanEntity.setReturnDate("2023-03-15");

        when(loanMapper.toEntity(inputDto)).thenReturn(loanEntity);
        when(repository.update(argThat(loan -> loan.getLoanId() == id &&
                "2023-03-01".equals(loan.getLoanDate()))))
                .thenReturn(true);

        boolean result = loanService.updateLoan(id, inputDto);
        assertTrue(result);

        verify(loanMapper).toEntity(inputDto);
        verify(repository).update(argThat(loan -> loan.getLoanId() == id &&
                "2023-03-01".equals(loan.getLoanDate())));
    }

    @Test
    void testDeleteLoan() {
        int id = 1;
        when(repository.delete(id)).thenReturn(true);
        boolean result = loanService.deleteLoan(id);
        assertTrue(result);
        verify(repository).delete(id);
    }
}
