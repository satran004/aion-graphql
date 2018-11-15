package org.satran.blockchain.graphql.rest.controllers;
import static org.satran.blockchain.graphql.rest.common.RestConstants.VERSION1_BASE_PATH;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.satran.blockchain.graphql.model.TxDetails;
import org.satran.blockchain.graphql.service.TxnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping(VERSION1_BASE_PATH + "transactions")
@ConditionalOnProperty(name = "rest.enable", havingValue = "true")
@Api(value="transactions", description="Transactions specific apis")
public class TransactionController {

  @Autowired
  private TxnService txnService;

  @GetMapping(produces = "application/hal+json")
  @ApiOperation(value = "Get transactions starting from the block number passed through \"before\" parameter")
  public ResponseEntity<Resources<TxDetails>> transactions(
      @RequestParam(value = "before", required = false, defaultValue = "-1") @ApiParam(value = "Start block number") Long before,
      @RequestParam(value = "first") @ApiParam(value = "No of transactions to return") Long first) {
    List<TxDetails> txDetailsList =  txnService.getTransactions(before, first);

    if(txDetailsList == null)
      return ResponseEntity.ok(null);

    for(TxDetails txDetails: txDetailsList) {
      txDetails.add(linkTo(methodOn(TransactionController.class).transaction(txDetails.getTxHash())).withSelfRel());
    }

    final Resources<TxDetails> txDetailsResources = new Resources<>(txDetailsList);

    txDetailsResources.add(
        linkTo(methodOn(TransactionController.class).transactions(before, first))
        .withSelfRel()
    );

    return ResponseEntity.ok(txDetailsResources);

  }

  @GetMapping(value = "/{txnHash}", produces = "application/json")
  @ApiOperation(value = "Get transaction details for the given transaction hash")
  public ResponseEntity<TxDetails> transaction(@PathVariable("txnHash") @ApiParam("Transaction hash")String txnHash) {

    TxDetails txDetails = txnService.getTransaction(txnHash);

    if(txnHash != null) {
      txDetails.add(linkTo(methodOn(TransactionController.class).transaction(txnHash)).withSelfRel());
      return ResponseEntity.ok(txDetails);
    } else {
      return (ResponseEntity<TxDetails>) ResponseEntity.notFound();
    }
  }

}
