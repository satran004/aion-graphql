package org.satran.blockchain.graphql.rest.controllers;

import static org.satran.blockchain.graphql.rest.common.RestConstants.VERSION1_BASE_PATH;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import org.satran.blockchain.graphql.model.Account;
import org.satran.blockchain.graphql.rest.exception.RestResourceNotFoundException;
import org.satran.blockchain.graphql.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(VERSION1_BASE_PATH + "accounts")
@ConditionalOnProperty(name = "rest.enable", havingValue = "true")
@Api(value="accounts", description="Account specific apis")
public class AccountController {

    private final static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping(value = "/{address}", produces = "application/json")
    @ApiOperation(value = "Get account by address")
    public ResponseEntity<Account> getAccount(@PathVariable("address")  String address) {

        try {
            List<String> fields = new ArrayList<>();
            fields.add("balance");
            fields.add("nonce");

            Account account = accountService.getAccount(address, fields, -1);

            if (account != null) {
                account.add(
                    linkTo(methodOn(AccountController.class).getAccount(address))
                        .withSelfRel()
                );

                return ResponseEntity.ok(account);
            } else {
                throw new RestResourceNotFoundException("Account not found");
            }
        }catch (Exception e) {
            logger.error("Error getting account details", e);
            throw e;
        }

    }
}
