package org.gfg.minor1.controller;

import org.gfg.minor1.exception.TxnServiceException;
import org.gfg.minor1.models.Student;
import org.gfg.minor1.request.CreateReturnTxnRequest;
import org.gfg.minor1.request.CreateTxnRequest;
import org.gfg.minor1.response.TxnSettlementResponse;
import org.gfg.minor1.service.TxnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/txn")
public class TxnController {

    @Autowired
    private TxnService txnService;
    private CreateReturnTxnRequest createReturnTxnRequest;
    @PostMapping("/create")
    public String create(@RequestBody CreateTxnRequest createTxnRequest) throws TxnServiceException {
        return txnService.create(createTxnRequest);
    }

    @PostMapping("/return")
    public TxnSettlementResponse returnBook(@RequestBody CreateReturnTxnRequest createReturnTxnRequest) throws TxnServiceException {
        this.createReturnTxnRequest = createReturnTxnRequest;
        return txnService.returnBook(createReturnTxnRequest);
    }
}
