<H1>GraphQL interface for Aion Blockchain</H1> 
<a href="https://travis-ci.org/satran004/aion-graphql"><img src="https://travis-ci.org/satran004/aion-graphql.svg?branch=master"/></a>

[![Join the chat at https://gitter.im/aion-graphql/Lobby](https://badges.gitter.im/aion-graphql/Lobby.svg)](https://gitter.im/aion-graphql/Lobby?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[Manual](https://docs.aion-graphql.com)

API Doc: <a href="https://satran004.github.io/aion-graphql-docs/"> Aion GraphQL API </a>

This project provides **GraphQL** endpoint to query Aion blockchain.
It is using AION java api to communicate with Aion blockchain.

It also support **REST Api** endpoints. The REST api development is still in progress. You can check supported REST apis here.

https://api.aion-graphql.com/swagger-ui.html

<b>Tested on:</b>
- Ubuntu 16.04 LTS, MacOS
- Java 10.x 

The following apis are currently supported :

- blockApi
- txnApi
- accountApi
- adminApi
- chainApi
- netApi
- walletApi
- contractApi

For detail information, please visit this <a href="https://satran004.github.io/aion-graphql-docs/"> API page </a>

The project will be enhanced to support other read and write operations.

### **1. Setup from Binary**

Download the latest release binary from [GitHub](https://github.com/satran004/aion-graphql/releases) repo.

`$> unzip aion-graphql-dist-[version].zip`

or

`$> tar xvf aion-graphql-dist-[version].tar`

Go to the extracted folder aion-graphql-dist-\[version\]

**Update Config**

Edit config/application.yml to provide aion kernel rpc host and port

> example: tcp://x.x.x.x:8547

**Start GraphQL server**

From aion-graphql-dist-\[version\] folder,  execute the following command to start the server.

`$> bin/aion-graphql`

_on Windows :_

`bin\aion-graphql.bat`

**Issue with Aion 0.3.2** : If you face any connnection issue with Aion Kernel 0.3.2, please check this issue [#10](https://github.com/satran004/aion-graphql/issues/10)  for workaround.

**Note:** _Make sure you start the server inside aion-graphql-dist-\[version\]  folder only. Starting server inside bin folder will not work._

### **2. Build from Source**

`$> git clone https://github.com/satran004/aion-graphql.git`

`$> ./gradlew clean build -x integrationTest`

_**To run**_ 

`$> export rpc_endpoint=tcp://[kernel-host]:8547`

`$> ./gradlew bootRun`

If you want to test GraphQL API on the browser, try the following url

`http://[host]:[port]/playground.html`

After running the above command, GraphQL endpoint can be accessed through the following url from your application :

`http://[host]:[port]/graphql`

### Demo
[![Aion GraphQL Playground Demo](https://img.youtube.com/vi/xnnz8EBMGjQ/0.jpg)](https://www.youtube.com/watch?v=xnnz8EBMGjQ)
