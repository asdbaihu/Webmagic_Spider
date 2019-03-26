package com.lenovo.common;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/***
 * 混播代理
 */
public class HunboProxy {
    private static String httpDownloadText(String url) {
        String result = "";
        try {
            // 根据地址获取请求
            HttpGet request = new HttpGet(url);//这里发送get请求
            // 获取当前客户端对象
            HttpClient httpClient = new DefaultHttpClient();
            // 通过请求对象获取响应对象
            HttpResponse response = httpClient.execute(request);

            // 判断网络连接状态码是否正常(0--200都数正常)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    // 创建下载者并且获取相对数量的代理IP
    // proxyCount 代理IP数量
    // leftTime 代理的最小生存时间，取决你的任务时间 单位是秒 最高最好不超过480
    public static HttpClientDownloader createDownloaderAndProxy(int proxyCount, int leftTime) {
        HttpClientDownloader ret = new HttpClientDownloader();
        if (proxyCount > 0) {

            Proxy[] prxs = new Proxy[proxyCount];
            String prxText = httpDownloadText("http://diaoxiuchong.v4.dailiyun.com/query.txt?key=NP64D615DC&word=&count=" + proxyCount + "&rand=true&detail=false&ltime=" + leftTime);
            String[] prxt = prxText.split("\r\n");

            for (int i = 0; i < proxyCount && i < prxt.length; i++) {
                String s = prxt[i];
                if (s != "" && s != null) {
                    String[] ms = s.split(":");
                    prxs[i] = new Proxy(ms[0], Integer.parseInt(ms[1]), "diaoxiuchong", "372928diao");
                }
            }

            ret.setProxyProvider(SimpleProxyProvider.from(prxs));
        }

        return ret;
    }
}
