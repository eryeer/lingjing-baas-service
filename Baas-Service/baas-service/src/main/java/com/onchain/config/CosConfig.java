package com.onchain.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CosConfig {

    private final ParamsConfig paramsConfig;

    @Bean
    public COSClient GetCosClient() {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(paramsConfig.cosSecretId, paramsConfig.cosSecretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        Region region = new Region(paramsConfig.cosRegionName);
        ClientConfig clientConfig = new ClientConfig(region);
        // set https for prod env
        if (StringUtils.equals(HttpProtocol.https.toString(), paramsConfig.cosHttpProtocol)) {
            clientConfig.setHttpProtocol(HttpProtocol.https);
        }
        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }
}
