package org.gfg.minor1.repository;

import org.gfg.minor1.models.Txn;
import org.gfg.minor1.models.TxnStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TxnRepository extends JpaRepository<Txn, Integer> {

    public Txn findByStudent_ContactAndBook_BookNoAndTxnStatusOrderByCreatedOnDesc(String studentContact,
                                                                                   String bookNo,
                                                                                   TxnStatus status);
}

// student with java book issued
// student is returning the book
// student is again issuinh the same book
