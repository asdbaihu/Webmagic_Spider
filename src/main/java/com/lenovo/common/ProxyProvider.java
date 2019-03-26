package com.lenovo.common;

import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/***
 * ������̬�����
 * �����ƴ���https://www.abuyun.com/
 */
public class ProxyProvider  {
    public static HttpClientDownloader createDownloaderAndProxy() {
        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(
                //HTTP�����������ַΪ��http-dyn.abuyun.com�����˿�Ϊ��9020��
                //ͨ��֤�飺H1ZT5X3356244N7D
                //ͨ����Կ��CCC97CDB4B85AD7E
                SimpleProxyProvider.from(
                        new Proxy("http-dyn.abuyun.com", 9020, "H1ZT5X3356244N7D", "CCC97CDB4B85AD7E")
                ));
        return httpClientDownloader;
    }
}
