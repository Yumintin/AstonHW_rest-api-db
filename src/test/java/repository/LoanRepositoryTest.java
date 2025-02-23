package repository;

import entity.Loan;
import org.junit.jupiter.api.*;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoanRepositoryTest {

    private LoanRepository loanRepository;

    @BeforeAll
    public void setUp() {
        loanRepository = new LoanRepository();
    }

    @BeforeEach
    public void clearData() {

    }

    @Test
    public void testCreateAndGetById() {
        Loan loan = new Loan();
        loan.setBookId(1);
        loan.setReaderId(1);
        loan.setLoanDate("2023-01-01");
        loan.setReturnDate("2023-01-15");

        Loan createdLoan = loanRepository.create(loan);
        Assertions.assertNotNull(createdLoan, "Созданный объект Loan не должен быть null");
        Assertions.assertNotNull(createdLoan.getLoanId(), "ID займа должен быть установлен после создания");

        Loan foundLoan = loanRepository.getById(createdLoan.getLoanId());
        Assertions.assertNotNull(foundLoan, "Займ должен быть найден по ID");
        Assertions.assertEquals(1, foundLoan.getBookId());
        Assertions.assertEquals(1, foundLoan.getReaderId());
        Assertions.assertEquals("2023-01-01", foundLoan.getLoanDate());
        Assertions.assertEquals("2023-01-15", foundLoan.getReturnDate());
    }

    @Test
    public void testUpdate() {
        Loan loan = new Loan();
        loan.setBookId(1);
        loan.setReaderId(2);
        loan.setLoanDate("2023-02-01");
        loan.setReturnDate("2023-02-15");

        Loan createdLoan = loanRepository.create(loan);
        createdLoan.setBookId(2);
        createdLoan.setReaderId(2);
        createdLoan.setLoanDate("2023-03-01");
        createdLoan.setReturnDate("2023-03-15");

        boolean updated = loanRepository.update(createdLoan);
        Assertions.assertTrue(updated, "Обновление займа должно пройти успешно");

        Loan updatedLoan = loanRepository.getById(createdLoan.getLoanId());
        Assertions.assertNotNull(updatedLoan, "После обновления займ должен находиться по ID");
        Assertions.assertEquals(2, updatedLoan.getBookId());
        Assertions.assertEquals(2, updatedLoan.getReaderId());
        Assertions.assertEquals("2023-03-01", updatedLoan.getLoanDate());
        Assertions.assertEquals("2023-03-15", updatedLoan.getReturnDate());
    }

    @Test
    public void testGetAll() {
        Loan loan1 = new Loan();
        loan1.setBookId(1);
        loan1.setReaderId(1);
        loan1.setLoanDate("2023-01-05");
        loan1.setReturnDate("2023-01-20");
        loanRepository.create(loan1);

        Loan loan2 = new Loan();
        loan2.setBookId(2);
        loan2.setReaderId(2);
        loan2.setLoanDate("2023-02-05");
        loan2.setReturnDate("2023-02-20");
        loanRepository.create(loan2);

        List<Loan> loans = loanRepository.getAll();
        Assertions.assertNotNull(loans, "Список займов не должен быть null");
        Assertions.assertTrue(loans.size() >= 2, "В базе должно быть минимум 2 займа");
    }

    @Test
    public void testDelete() {
        Loan loan = new Loan();
        loan.setBookId(1);
        loan.setReaderId(1);
        loan.setLoanDate("2023-04-01");
        loan.setReturnDate("2023-04-15");

        Loan createdLoan = loanRepository.create(loan);
        boolean deleted = loanRepository.delete(createdLoan.getLoanId());
        Assertions.assertTrue(deleted, "Удаление займа должно пройти успешно");

        Loan foundLoan = loanRepository.getById(createdLoan.getLoanId());
        Assertions.assertNull(foundLoan, "После удаления займ не должен быть найден");
    }
}
