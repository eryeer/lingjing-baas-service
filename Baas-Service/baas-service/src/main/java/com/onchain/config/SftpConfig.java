package com.onchain.config;

import com.onchain.util.SFTPUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SftpConfig {

    final ParamsConfig paramsConfig;

    @Bean
    public SFTPUtil getSFTPUtil() {
        return new SFTPUtil(paramsConfig.pdfsUsername, paramsConfig.pdfsPassword, paramsConfig.pdfsHost, paramsConfig.pdfsPort);
    }
}
