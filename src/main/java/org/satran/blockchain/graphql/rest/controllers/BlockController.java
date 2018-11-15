package org.satran.blockchain.graphql.rest.controllers;

import static org.satran.blockchain.graphql.rest.common.RestConstants.VERSION1_BASE_PATH;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Collections;
import java.util.List;
import org.satran.blockchain.graphql.model.Block;
import org.satran.blockchain.graphql.model.TxDetails;
import org.satran.blockchain.graphql.rest.exception.RestResourceNotFoundException;
import org.satran.blockchain.graphql.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(VERSION1_BASE_PATH + "blocks")
@ConditionalOnProperty(name = "rest.enable", havingValue = "true")
@Api(value="blocks", description="Block specific apis")
public class BlockController {

  @Autowired
  private BlockService service;

  @GetMapping(produces = "application/hal+json")
  @ApiOperation(value = "Get blocks starting from the block number passed through \"before\" parameter")
  public ResponseEntity<Resources<Block>> getBlocks(
      @RequestParam(value = "first", required = false, defaultValue = "20") @ApiParam(value = "No of blocks to return") Long first,
      @RequestParam(value = "before", required = false, defaultValue = "-1") @ApiParam(value = "Start block number") Long before) {

    List<Block> blocks = service.getBlocks(first, before);

    if(blocks == null)
      return ResponseEntity.ok(null);

    for(Block block: blocks) {
      block.add(linkTo(methodOn(BlockController.class).getBlock(block.getNumber())).withSelfRel());

      block.getTxDetails().stream().forEach(txDetails -> {
        addTxnDetailsLink(txDetails);
      });
    }

    final Resources<Block> blockResources = new Resources<>(blocks);

    blockResources.add(
       linkTo(methodOn(BlockController.class).getBlocks(first, before))
           .withSelfRel()
    );
    return ResponseEntity.ok(blockResources);
  }

  @GetMapping(value = "/{number}", produces = "application/json")
  @ApiOperation(value = "Get block details for the given block number")
  public HttpEntity<Block> getBlock(@PathVariable("number") @ApiParam("Block number") Long number) {

    Block block = service.getBlock(number);

    if(block != null) {
      block.add(linkTo(methodOn(BlockController.class).getBlock(number)).withSelfRel());

      block.getTxDetails().stream().forEach(txDetails -> {
        addTxnDetailsLink(txDetails);
      });

      return new ResponseEntity<>(block, HttpStatus.OK);
    } else {
      throw new RestResourceNotFoundException("Block not found");
    }
  }

  private void addTxnDetailsLink(TxDetails txDetails) {
    txDetails.add(linkTo(methodOn(TransactionController.class).transaction(txDetails.getTxHash())).withSelfRel());

    txDetails.add(
        linkTo(methodOn(AccountController.class).getAccount(txDetails.getFrom()))
            .withRel("from"));

    txDetails.add(
        linkTo(methodOn(AccountController.class).getAccount(txDetails.getTo()))
            .withRel("to"));
  }

}
