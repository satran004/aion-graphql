package org.satran.blockchain.graphql.resolvers;

import org.satran.blockchain.graphql.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WalletMutator {

    private static final Logger logger = LoggerFactory.getLogger(WalletMutator.class);

    private WalletService walletService;

    public WalletMutator(WalletService walletService) {
        this.walletService = walletService;
    }


    public boolean lockAccount(String pubAddress, String passphrase) {
        return walletService.lockAccount(pubAddress, passphrase);
    }

    public boolean unlockAccount(String pubAddress, String passphrase, int duration) {
        return walletService.unlockAccount(pubAddress, passphrase, duration);
    }
}
