package com.lenovo.common;

import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/***
 * 创建动态代理池
 * 阿布云代理：https://www.abuyun.com/
 */
public class ProxyProvider  {
    public static HttpClientDownloader createDownloaderAndProxy() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(
                //HTTP隧道服务器地址为『http-dyn.abuyun.com』，端口为『9020』
                //通行证书：H1ZT5X3356244N7D
                //通行密钥：CCC97CDB4B85AD7E
                SimpleProxyProvider.from(
                        new Proxy("http-dyn.abuyun.com", 9020, "H1ZT5X3356244N7D", "CCC97CDB4B85AD7E")
                ));
        return httpClientDownloader;
    }
}
