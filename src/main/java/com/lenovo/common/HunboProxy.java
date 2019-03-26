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
 * �첥����
 */
public class HunboProxy {
    private static String httpDownloadText(String url) {
        String result = "";
        try {
            // ���ݵ�ַ��ȡ����
            HttpGet request = new HttpGet(url);//���﷢��get����
            // ��ȡ��ǰ�ͻ��˶���
            HttpClient httpClient = new DefaultHttpClient();
            // ͨ����������ȡ��Ӧ����
            HttpResponse response = httpClient.execute(request);

            // �ж���������״̬���Ƿ�����(0--200��������)
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

    // ���������߲��һ�ȡ��������Ĵ���IP
    // proxyCount ����IP����
    // leftTime �������С����ʱ�䣬ȡ���������ʱ�� ��λ���� �����ò�����480
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
