<H1>GraphQL interface for Aion Blockchain</H1> 
<a href="https://travis-ci.org/satran004/aion-graphql"><img src="https://travis-ci.org/satran004/aion-graphql.svg?branch=master"/></a>

Demo: http://graphql.aion-tools.info/playground.html

API Doc: <a href="https://satran004.github.io/aion-graphql-docs/www/"> Aion GraphQL API </a>

This project provides GraphQL REST endpoint to query Aion blockchain.
It is using AION java api to communicate with Aion blockchain.


<b>Tested on:</b>
- Ubuntu 16.04 LTS, MacOS
- Java 10.x

This project is under active development. 

The following apis are currently supported :

- blockApi
- txnApi
- accountApi
- adminApi
- chainApi
- netApi
- walletApi
- contractApi

For detail information, please visit this <a href="https://satran004.github.io/aion-graphql-docs/www/"> API page </a>

The project will be enhanced to support other read and write operations.

<H3>Build From Source</H3>

<b>Compile:</b>

$> ./gradlew clean build

<b>Run:</b>

$> rpc_endpoint=tcp://[kernel-host]:8547 ./gradlew bootRun


