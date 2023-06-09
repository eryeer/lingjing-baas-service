package com.onchain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class Web3jConfig {

    @Value("${chain.maas.rpcUrl}")
    private String nodeAddress;

    @Bean
    public Web3j getHttpWeb3j() {
        return Web3j.build(new HttpService(nodeAddress));
    }

}
