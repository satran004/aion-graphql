package org.satran.blockchain.graphql.model;

import org.satran.blockchain.graphql.model.input.TxArgsInput;

public class ContractDeployPayload {

    private TxArgsInput txArgsInput;

    private CompileResponseBean compileResponseBean;

    public ContractDeployPayload() {

    }

    public ContractDeployPayload(TxArgsInput txArgsInput, CompileResponseBean compileResponseBean) {
        this.txArgsInput = txArgsInput;
        this.compileResponseBean = compileResponseBean;
    }

    public TxArgsInput getTxArgsInput() {
        return txArgsInput;
    }

    public void setTxArgsInput(TxArgsInput txArgsInput) {
        this.txArgsInput = txArgsInput;
    }

    public CompileResponseBean getCompileResponseBean() {
        return compileResponseBean;
    }

    public void setCompileResponseBean(
        CompileResponseBean compileResponseBean) {
        this.compileResponseBean = compileResponseBean;
    }
}
