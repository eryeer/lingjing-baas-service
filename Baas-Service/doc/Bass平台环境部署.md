# Baas平台环境部署

## 环境依赖和中间件

1. Mysql

    目前在 172.168.3.12，
    包含数据库 dna2_baas和dna2_explorer， 
    需要把数据迁移到新环境，
    迁移后后端应用需修改相关配置

2. Redis

    目前在 172.168.3.12，
    迁移后后端应用需修改相关配置

3. jdk 1.8.0_241

    目前在 172.168.3.12，可以打包拷贝，然后设置系统路径
    ```
    $ cd /opt/jdk
    ```

4. Nacos 

    目前在 172.168.3.12，可以打包拷贝

    使用单节点模式启动 
    ```
    $ cd /opt/nacos/bin
    $ bash startup.sh -m standalone
    ```

    迁移后后端应用需修改相关配置

5. ZipKin

    目前在 172.168.3.12，可以打包拷贝
    ``` 
    $ cd /opt/zipkin
    $ ./start.sh
    ```

## Baas后端服务（172.168.3.13:/opt/baas）

文件夹整体copy之后修改对应的start.sh文件配置

1. baas-service.jar

    ``` script
    $ cd /opt/baas
    $ ./startBaasService.sh
    ```

2. baas-gateway.jar
    
    ``` script
    $ cd /opt/baas
    $ ./startGateWay.sh
    ```

3. baas-crosschain.jar
    
    ``` script
    $ cd /opt/baas
    $ ./startCrosschain.sh
    ```
    
4. dna2-explorer.jar
    
    ``` script
    $ cd /opt/baas
    $ ./startExplorer.sh
    ```


## PDFS

1. PDFS-CLI

    目前在 172.168.3.13:/home/ubuntu/pdfs/pdfs-cli
    需要迁移到新服务器，重新启动即可

    ```
    ./start.sh
    ./pdfs-cli service start
    ```
    
## 跨链服务

1. DNA 1.0

    目前在 172.168.3.64，172.168.3.65，172.168.3.66，172.168.3.67 ，
    需要迁移到新服务器，节点关闭日志清理后整体打包再启动即可

    在 startCrosschain.sh 中添加 
    -DDNA_V_ONE_URL=http://172.168.3.67:20334

2. Frabic

    迁移到新环境后需配置host url
    ```
    $ vi /etc/hosts
    ...
    // 添加
    43.134.97.58    peer0.org1.example.com ca.org1.example.com orderer.example.com peer0.org2.example.com ca.org2.example.com
    ``

3. RNA

    目前在 http://43.134.97.58:21132
    迁移不做修改

4. POLY Test

    在 startCrosschain.sh 中添加 -DPOLY_TESTNET_URL=https://maas-test-node.onchain.com 配置

5. KOVAN

    迁移不做修改