package com.lenovo.test;

import com.lenovo.common.SeleniumDownloader;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Collections;
import java.util.List;

/**
 *�����ղ���
 */
public class AiQiYiTest implements PageProcessor {

    public static final String URL_LIST = "https://list.iqiyi.com/www/1/-------------24-\\d+\\-1-iqiyi--.html";
    public static final String URL_POST = "https://www.iqiyi.com/v_\\w+\\.html";

    private Site site = Site
            .me()
            .setDomain("www.iqiyi.com")
            .setUseGzip(true)
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("Upgrade-Insecure-Requests", "1")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36");

    @Override
    public void process(Page page) {
        Selectable selectUrl = page.getUrl();
//        if(selectUrl.regex(URL_LIST).match()) {
//            // ��ȡ��Ӱ�б�
//            List<Selectable> nodes = page.getHtml().links().regex(URL_POST).nodes();
//            if (!nodes.isEmpty()) {
//                // ��ǰ���б�ҳ
//                for (Selectable node : nodes) {
//                    // ��ÿһ��ְλ������ҳ���ӻ�ã�Ȼ�������Ϣ������
//                    String itemUrl = node.links().toString();
//                    page.addTargetRequest(itemUrl);
//                }
//                // ���Ƿ�ҳ����ҳ��ť����Ӧ������Ҳ�ǽ��뵽�б�ҳ��
//                List<String> pageList = page.getHtml().css("div.csPpFeed_comPage span.bk").links().all();
//                // ����ҳ������Ҳ������Ϣ����
//                page.addTargetRequests(pageList);
//            }
//        }
        //�б�ҳ
        if(selectUrl.regex(URL_LIST).match()){
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            List<String> itemlist = page.getHtml().links().regex(URL_POST).all();
            if(!itemlist.isEmpty()) {
                for (String item : itemlist) {
                    page.addTargetRequests(Collections.singletonList(item));
                }
            }
            // ���Ƿ�ҳ����ҳ��ť����Ӧ������Ҳ�ǽ��뵽�б�ҳ��
            //List<String> pageList = page.getHtml().css("div.csPpFeed_comPage span.bk").links().all();
           // List<String> pageList = page.getHtml().css("div.csPpFeed_comPage span.page a1").links().all();
            List<String> pageList = page.getHtml().xpath("//*[@id=\"block-D\"]/div/span[12]").links().all();
            // ����ҳ������Ҳ������Ϣ����
            page.addTargetRequests(pageList);
        }
        //����ҳ
        else if(selectUrl.regex(URL_POST).match()) {
            //page.putField("", page.getHtml().xpath("//div[@id='root']//div[@class='List']/tidyText()"));
            page.putField("��Ӱ����", page.getHtml().xpath("//*[@id=\"block-L\"]/div[2]/div[1]/ul/li[1]/h3/tidyText()"));
            page.putField("����", page.getHtml().xpath("//*[@id=\"block-L\"]/div[2]/div[1]/ul/li[2]/span/tidyText()"));
            page.putField("����", page.getHtml().xpath("//*[@id=\"block-L\"]/div[2]/div[1]/ul/li[3]/span/tidyText()"));
            page.putField("����", page.getHtml().xpath("//*[@id=\"block-L\"]/div[2]/div[1]/ul/li[4]/span/tidyText()"));
            page.putField("��ӳ", page.getHtml().xpath("//*[@id=\"block-L\"]/div[2]/div[1]/ul/li[5]/span/tidyText()"));
            page.putField("����", page.getHtml().xpath("//*[@id=\"block-L\"]/div[2]/div[2]/span[2]/span[1]/allText()"));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        System.setProperty("selenuim_config", "D:\\develop\\code\\Demo\\src\\main\\resources\\selenium_test.properties");
        //System.setProperty("selenuim_config", "/shuju/jar/selenium.properties");

       // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(
                SimpleProxyProvider.from(
                        new Proxy("http-dyn.abuyun.com", 9020, "H1ZT5X3356244N7D", "CCC97CDB4B85AD7E")
                ));

        Spider spider = Spider.create(new AiQiYiTest())
                .addUrl("https://list.iqiyi.com/www/1/-------------24-1-1-iqiyi--.html")
                .addPipeline(new FilePipeline("D:\\jianshu"))
                //.addPipeline(new FilePipeline("/data/dynamic_spider/zhihu"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(
                        new BloomFilterDuplicateRemover(10000)))
                .setScheduler(new FileCacheQueueScheduler("D:\\jianshu\\urlfile"))
                //.setScheduler(new FileCacheQueueScheduler("/data/dynamic_spider/zhihu/urlfile"))
                 .setDownloader(httpClientDownloader)
                //.setDownloader(new SeleniumDownloader02("E:\\chromedriver_win32\\chromedriver.exe").setSleepTime(1000))
                .setDownloader(new SeleniumDownloader())
                .thread(5);

        Scheduler scheduler = spider.getScheduler();
        spider.run();
    }
}