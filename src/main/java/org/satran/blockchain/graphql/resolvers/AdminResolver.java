package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.exception.DataFetchingException;
import org.satran.blockchain.graphql.model.Account;
import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.service.AdminService;
import org.satran.blockchain.graphql.service.ChainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminResolver {

    private Logger logger = LoggerFactory.getLogger(AdminResolver.class);

    private AdminService adminService;

    public AdminResolver(AdminService adminService) {
        this.adminService = adminService;
    }

    //comma separated addresses
    public List<Account> accountsByAddressString(String addresses) {
        try {
            return adminService.getAccountDetailsByAddressList(addresses);
        } catch (Exception e) {
            logger.error("Excption while fetching accounts ", e);
            throw new DataFetchingException(e.getMessage());
        }
    }

    public List<Account> accounts(List<String> addresses) {
        try {
            return adminService.getAccountDetailsByAddressList​(addresses);
        } catch (Exception e) {
            logger.error("Excption while fetching accounts ", e);
            throw new DataFetchingException(e.getMessage());
        }
    }

    public Block blockByHash(String hash) {
        try {
            return adminService.getBlockDetailsByHash(hash);
        } catch (Exception e) {
            logger.error("error getting block by hash", e);
            throw new DataFetchingException(e.getMessage());
        }
    }

    public List<Block> blocksByLatest(long count) {
        return adminService.getBlockDetailsByLatest(count);
    }

    public Block blockByNumber(long blockNumber) {
        return adminService.getBlockDetailsByNumber(blockNumber);
    }

    public List<Block> blocksByNumber(List<Long> blockNumbers) {
        return adminService.getBlockDetailsByNumber(blockNumbers);
    }

    public List<Block> blocksByRange(long blockStart, long blockEnd) {
        return adminService.getBlockDetailsByRange(blockStart, blockEnd);
    }

    public List<Block> blocks(Long first, Long offset) {
        return adminService.getBlocks(first, offset);
    }



}
