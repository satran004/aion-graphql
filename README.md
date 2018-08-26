<H1>GraphQL interface for Aion Blockchain</H1> 
<img src="https://travis-ci.org/satran004/aion-graphql.svg?branch=master"/>

Demo Setup: http://graphql.aion-tools.info/graphiql

This project provides GraphQL REST endpoint to query Aion blockchain.
It is using AION java api to communicate with Aion blockchain.


<b>Requirement:</b>
- Ubuntu 16.04 LTS and above
- Java 10.x

The implementation is in very early stage.

Currently, only the following read-only operations are supported:
- Get blocks
- Get block by hash
- Get Transaction by hash


The project will be enhanced to support other read and write operations.

<H3>Build From Source</H3>

<b>Compile:</b>

$> mvn clean build

<b>Run:</b>

$> mvn spring-boot:run -Dspring-boot.run.arguments=--rpc.endpoint=tcp://[kernel-host]:8547




