package org.gfg.minor1.service;

import org.gfg.minor1.exception.TxnServiceException;
import org.gfg.minor1.models.*;
import org.gfg.minor1.repository.TxnRepository;
import org.gfg.minor1.request.CreateReturnTxnRequest;
import org.gfg.minor1.request.CreateTxnRequest;
import org.gfg.minor1.response.TxnSettlementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TxnService {

    @Autowired
    private StudentService studentService;

    @Autowired
    private BookService bookService;

    @Autowired
    private TxnRepository txnRepository;

    @Value("${student.valid.days}")
    private String ValidDays;
    @Value("${student.perday.fine}")
    private Integer finePerDay;

    @Transactional(rollbackFor = {TxnServiceException.class})
    public String create(CreateTxnRequest createTxnRequest) throws TxnServiceException {

        Student student = findStudent(createTxnRequest.getStudentContact());
        Book book = findBook(createTxnRequest.getBookNo());

        Txn txn = Txn.builder().
                student(student).
                book(book).
                txnId(UUID.randomUUID().toString()).
                paidCost(createTxnRequest.getPaidAmount()).
                txnStatus(TxnStatus.ISSUED).
                build();

        book.setStudent(student);
        //saving the data of book to book table 1) query
        bookService.createUpdate(book);
        // trying to save the data inside the txn 2) queries
        return txnRepository.save(txn).getTxnId();

    }
    public Student findStudent(String studentContact) throws TxnServiceException {
        List<Student> studentList = studentService.findStudent(StudentFilterType.CONTACT,
                studentContact,
                OperationType.EQUALS);
        if (studentList == null || studentList.isEmpty()) {
            throw new TxnServiceException("Student does not exist in the library");
        }
        return studentList.get(0);
    }
    public Book findBook(String bookNo) throws TxnServiceException {
        List<Book> listBook = bookService.findBooks(BookFilterType.BOOK_NO,
                bookNo,
                OperationType.EQUALS);

        if(listBook == null || listBook.isEmpty() || listBook.get(0).getStudent() != null){
            throw new TxnServiceException("Book does not exist in the library");
        }
        return listBook.get(0);
    }
    public Book findBookForReturn(String bookNo) throws TxnServiceException {
        List<Book> listBook = bookService.findBooks(BookFilterType.BOOK_NO,
                bookNo,
                OperationType.EQUALS);

        if(listBook == null || listBook.isEmpty()){
            throw new TxnServiceException("Book does not exist in the library");
        }
        return listBook.get(0);
    }
    @Transactional
    public TxnSettlementResponse returnBook(CreateReturnTxnRequest createReturnTxnRequest) throws TxnServiceException {
        Student student = findStudent(createReturnTxnRequest.getStudentContact());
        Book book = findBookForReturn(createReturnTxnRequest.getBookNo());
        Txn txn =txnRepository.findByStudent_ContactAndBook_BookNoAndTxnStatusOrderByCreatedOnDesc
                (student.getContact(), book.getBookNo(), TxnStatus.ISSUED);
        // new entry in txn table
        // object what was the amount & was it returned /fined

        // if settlement amount < txn.paidamount --> fined case

        // update the table txn
        int settlementAmount = calculateSettlementAmount(txn);
        txn.setTxnStatus(settlementAmount == txn.getPaidCost() ? TxnStatus.RETURNED : TxnStatus.FINED);
        txn.setPaidCost(settlementAmount);
        txnRepository.save(txn);

        // book available
        book.setStudent(null);
        bookService.createUpdate(book);
        return TxnSettlementResponse.builder().
                txnId(txn.getTxnId()).
                settlementAmount(settlementAmount).
                build();
    }

    private int calculateSettlementAmount(Txn txn){

        long issueTime = txn.getCreatedOn().getTime();
        long returnTime = System.currentTimeMillis();

        long diff = returnTime-issueTime;
        int daysPassed = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        if(daysPassed > Integer.valueOf(ValidDays)){
            int amount = (daysPassed-Integer.valueOf(ValidDays) * finePerDay );
            return txn.getPaidCost()-amount;

        }
        return txn.getPaidCost();

    }

}

