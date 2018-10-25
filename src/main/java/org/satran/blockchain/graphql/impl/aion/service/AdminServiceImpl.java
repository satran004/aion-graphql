package org.satran.blockchain.graphql.impl.aion.service;

import org.aion.api.type.AccountDetails;
import org.aion.api.type.BlockDetails;
import org.aion.base.type.Address;
import org.aion.base.type.Hash256;
import org.satran.blockchain.graphql.impl.aion.service.dao.AionBlockchainAccessor;
import org.satran.blockchain.graphql.exception.BlockChainAcessException;
import org.satran.blockchain.graphql.impl.aion.util.ModelConverter;
import org.satran.blockchain.graphql.model.Account;
import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AdminServiceImpl implements AdminService {

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    private final AionBlockchainAccessor accessor;

    public AdminServiceImpl(AionBlockchainAccessor accessor) {
        this.accessor = accessor;
    }

    @Override
    public List<Account> getAccountDetailsByAddressList(String addresses) {
        if (logger.isDebugEnabled())
            logger.debug("Getting accounts");

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getAdmin().getAccountDetailsByAddressList(addresses));

            if (apiMsg.isError()) {
                logger.error("Unable to get accounts" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Account details fetched");

            List<AccountDetails> accountDetails = apiMsg.getObject();

            return accountDetails.stream()
                    .map(ad -> {
                        Account acc = new Account();
                        acc.setBalance(ad.getBalance());
                        acc.setAddress(ad.getAddress().toString());

                        return acc;
                    }).collect(Collectors.toList());
        }));
    }

    @Override
    public List<Account> getAccountDetailsByAddressList​(List<String> addresses) {
        if (logger.isDebugEnabled())
            logger.debug("Getting accounts");

        List<Address> addressList = addresses.stream()
                                        .map(add -> Address.wrap(add))
                                        .collect(Collectors.toList());

        return accessor.call(((apiMsg, api) -> {
            apiMsg.set(api.getAdmin().getAccountDetailsByAddressList(addressList));

            if (apiMsg.isError()) {
                logger.error("Unable to get accounts" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Account details fetched");

            List<AccountDetails> accountDetails = apiMsg.getObject();

            return accountDetails.stream()
                    .map(ad -> {
                        Account acc = new Account();
                        acc.setBalance(ad.getBalance());
                        acc.setAddress(ad.getAddress().toString());

                        return acc;
                    }).collect(Collectors.toList());
        }));
    }

    @Override
    public Block getBlockDetailsByHash(String hash) {

       if(logger.isDebugEnabled())
            logger.debug("Getting block for {}", hash);

        return accessor.call(((apiMsg, api) -> {

            apiMsg.set(api.getAdmin().getBlockDetailsByHash(Hash256.wrap(hash)));
            if (apiMsg.isError()) {
                logger.error("Unable to get the block" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Block details got" + apiMsg.getObject().getClass());

            List<BlockDetails> blkDetails = ((List<BlockDetails>) apiMsg.getObject());

            if (blkDetails == null || blkDetails.size() == 0)
                throw new BlockChainAcessException("No block found with hash : " + hash);

            BlockDetails block = blkDetails.get(0);

            Block b = ModelConverter.convert(block);
            return b;
        }));
    }

    @Override
    public List<Block> getBlockDetailsByLatest(long count) {
        return getBlocks(count, -1);
    }

    @Override
    public Block getBlockDetailsByNumber(long blockNumber) {
        if(logger.isDebugEnabled())
            logger.debug("Getting block for {} ", blockNumber);

        return accessor.call(((apiMsg, api) -> {

            apiMsg.set(api.getAdmin().getBlockDetailsByNumber(String.valueOf(blockNumber)));
            if (apiMsg.isError()) {
                logger.error("Unable to get the block" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Block details got" + apiMsg.getObject().getClass());

            List<BlockDetails> blkDetails = ((List<BlockDetails>) apiMsg.getObject());

            if (blkDetails == null || blkDetails.size() == 0)
                throw new BlockChainAcessException("No block found with number : " + blockNumber);

            BlockDetails block = blkDetails.get(0);

            Block b = ModelConverter.convert(block);
            return b;
        }));
    }

    @Override
    public List<Block> getBlockDetailsByNumber(List<Long> blockNumbers) {
        if(logger.isDebugEnabled())
            logger.debug("Getting block for {} ", blockNumbers);

        return accessor.call(((apiMsg, api) -> {

            apiMsg.set(api.getAdmin().getBlockDetailsByNumber(blockNumbers));
            if (apiMsg.isError()) {
                logger.error("Unable to get the block" + apiMsg.getErrString());
                throw new BlockChainAcessException(apiMsg.getErrString());
            }

            if (logger.isDebugEnabled())
                logger.debug("Block details got" + apiMsg.getObject().getClass());

            List<BlockDetails> blkDetails = ((List<BlockDetails>) apiMsg.getObject());

            if (blkDetails == null || blkDetails.size() == 0)
                throw new BlockChainAcessException("No block found with numbers");

            return blkDetails.stream()
                    .map(bd -> ModelConverter.convert(bd))
                    .collect(Collectors.toList());
        }));
    }

    @Override
    public List<Block> getBlockDetailsByRange(long blockStart, long blockEnd) {
        if(logger.isDebugEnabled())
            logger.debug("Getting block from {} to {} ", blockStart, blockEnd);

        return accessor.call(((apiMsg, api) -> {

            apiMsg.set(api.getAdmin().getBlockDetailsByRange(blockStart, blockEnd));


            if (apiMsg.isError()) {
                logger.error("Error getting block details" + apiMsg.getErrString());
            }

            List<BlockDetails> block = (List<BlockDetails>) apiMsg.getObject();

            if (logger.isDebugEnabled())
                logger.debug("Result: " + block.toString());

            return block.stream()
                    .map(blockDetails -> ModelConverter.convert(blockDetails))
                    .collect(Collectors.toList());

        }));
    }


    @Override
    public  List<Block> getBlocks(Long first, long before) {


        if(first > 50)
            throw new BlockChainAcessException("Too many blocks. You can only request upto 50 blocks in a call");


        //Find the latest block number if the blocknumber is not passed
       /* if(offset == -1) {

            long bn = accessor.call(((apiMsg, api) -> {
                apiMsg.set(api.getChain().blockNumber());
                if (apiMsg.isError()) {
                    logger.error("Get blockNumber isError: " + apiMsg.getErrString());
                }

                return api.getChain().blockNumber().getObject();
            }));
        }*/


        return accessor.call(((apiMsg, api) -> {

            if (before == -1)
                apiMsg.set(api.getAdmin().getBlockDetailsByLatest(first));
            else {
                apiMsg.set(api.getAdmin().getBlockDetailsByRange(before - first, before-1));
            }

            if (apiMsg.isError()) {
                logger.error("Error getting block details" + apiMsg.getErrString());
            }

            List<BlockDetails> blocks = (List<BlockDetails>) apiMsg.getObject();

            if (logger.isDebugEnabled())
                logger.debug("Result: " + blocks.toString());

            if(blocks != null)
                Collections.reverse(blocks);

            return blocks.stream()
                    .map(blockDetails -> ModelConverter.convert(blockDetails))
                    .collect(Collectors.toList());

        }));

    }
}
