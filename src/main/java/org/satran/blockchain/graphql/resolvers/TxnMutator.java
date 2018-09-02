package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.model.CompileResponseBean;
import org.satran.blockchain.graphql.model.MsgRespBean;
import org.satran.blockchain.graphql.model.input.TxArgsInput;
import org.satran.blockchain.graphql.service.TxnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public  class TxnMutator {
    private Logger logger = LoggerFactory.getLogger(TxnMutator.class);

    private TxnService txnService;

    public TxnMutator(TxnService txnService) {
        this.txnService = txnService;
    }

    public CompileResponseBean compile(String code) {
        return txnService.compile(code);
    }

    public MsgRespBean sendTransaction(TxArgsInput txArgsInput) {
        return txnService.sendTransaction(txArgsInput);
    }

}