package com.onchain.dna2explorer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    @Value("${dna.node.restUrl}")
    private String nodeAddress;

    @Value("${dna.node.syncNodeUrl}")
    private String syncNodeUrl;

    public String getSyncNodeUrl() {
        return syncNodeUrl;
    }

    public String getNodeAddress(){
        return nodeAddress;
    }

    @Bean
    public Web3j getHttpWeb3j() {
        return Web3j.build(new HttpService(nodeAddress));
    }
}
