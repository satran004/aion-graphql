package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.type.CompileResponse;
import org.aion.api.type.MsgRsp;
import org.aion.api.type.Transaction;
import org.aion.api.type.TxArgs;
import org.aion.base.type.Address;
import org.aion.base.type.Hash256;
import org.aion.base.util.ByteArrayWrapper;
import org.satran.blockchain.graphql.impl.aion.service.dao.AionBlockchainAccessor;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.model.CompileResponseBean;
import org.satran.blockchain.graphql.model.MsgRespBean;
import org.satran.blockchain.graphql.model.TxDetails;
import org.satran.blockchain.graphql.model.input.TxArgsInput;
import org.satran.blockchain.graphql.service.BlockService;
import org.satran.blockchain.graphql.service.TxnService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TxnServiceImpl implements TxnService {

    private static final Logger logger = LoggerFactory.getLogger(TxnServiceImpl.class);

    private BlockService chainService;

    private AionBlockchainAccessor accessor;

    public TxnServiceImpl(BlockService blockService, AionBlockchainAccessor accessor) {
        this.chainService = blockService;
        this.accessor = accessor;
    }

    public List<TxDetails> getTransactions(long fromBlock, long limit) {
        List<TxDetails> transactions = new ArrayList<TxDetails>();

        if (logger.isDebugEnabled())
            logger.debug("Getting transaction -----------");

        if (fromBlock == -1) {
            Block latestBlock = chainService.getLatestBlock();

            if (logger.isDebugEnabled())
                logger.debug("Return block " + latestBlock.getNumber());

            if (latestBlock != null)
                fromBlock = latestBlock.getNumber();

        }

        while (transactions.size() < limit && fromBlock >= 0) {
            Block blockDetails = chainService.getBlock(fromBlock);

            if (blockDetails == null)
                break;


            if (blockDetails.getTxDetails().size() > 0) {
                transactions.addAll(blockDetails.getTxDetails());
            }

            fromBlock--;
        }

        return transactions;

    }

    public TxDetails getTransaction(String txHash) {
        if (logger.isDebugEnabled())
            logger.debug("Getting transaction for {} ", txHash);

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getChain().getTransactionByHash(Hash256.wrap(txHash)));
            if (apiMsg.isError()) {
                logger.error("Unable to get the transaction" + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Transaction details" + apiMsg.getObject());

            Transaction transaction = apiMsg.getObject();

            return ModelConverter.convert(transaction);
//            if(blkDetails == null || blkDetails.size() == 0)
//                throw new RuntimeException("No block found with number : " + number);
//
//            BlockDetails block = blkDetails.get(0);
//
//            return block;


        }));

    }

    public CompileResponseBean compile(String code) {
        if(logger.isDebugEnabled())
            logger.debug("Trying to compile code");

        final String code1 = "pragma solidity ^0.4.22;\n" +
                "\n" +
                "contract helloWorld {\n" +
                " function renderHelloWorld () public pure returns (string) {\n" +
                "   return 'helloWorld';\n" +
                " }\n" +
                "}";

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getTx().compile(code1));

            if(apiMsg.isError()) {
                logger.error("Error compiling contract source code : {} " + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

            CompileResponse response = apiMsg.getObject();

            CompileResponseBean responseBean = new CompileResponseBean();
            responseBean.setCode(response.getCode());
            responseBean.setCompilerVersion(response.getCompilerVersion());

            return responseBean;
        }));

    }

    @Override
    public MsgRespBean sendTransaction(TxArgsInput txArgsInput) {
        if (logger.isDebugEnabled())
            logger.debug("Sending transaction : {} ", txArgsInput);

        return accessor.call(((apiMsg, api) -> {

            TxArgs.TxArgsBuilder txArgsBuilder = new TxArgs.TxArgsBuilder()
                    .from(Address.wrap(txArgsInput.getFrom()))
                    .to(Address.wrap(txArgsInput.getTo()))
                    .value(txArgsInput.getValue())
                    .nrgLimit(txArgsInput.getNrgLimit())
                    .nrgPrice(txArgsInput.getNrgPrice());

            if(txArgsInput.getData() != null)
                txArgsBuilder.data(ByteArrayWrapper.wrap(txArgsInput.getData().getBytes()));

            if(txArgsInput.getNonce() != null)
                txArgsBuilder.nonce(txArgsInput.getNonce());

            TxArgs txArgs = txArgsBuilder.createTxArgs();

            apiMsg.set(api.getTx().nonBlock().sendTransaction(txArgs));

            if(apiMsg.isError()) {
                logger.error("Error posting transaction : {} " + apiMsg.getErrString());
                throw new RuntimeException(apiMsg.getErrString());
            }

//
            MsgRsp msgRsp = apiMsg.getObject();

            if(logger.isDebugEnabled())
                logger.debug("Posted transaction hash : {}", msgRsp.getTxHash());

            return ModelConverter.convert(msgRsp);
        }));
    }
}
