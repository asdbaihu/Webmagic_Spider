package com.lenovo.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Selectable;

/**
 *Öªºõ²âÊÔ
 */
public class ZhiHuTest implements PageProcessor {

    //public static final String URL_LIST = "https://www.jianshu.com/u/\\w+\\?utm_campaign=maleskine&utm_content=user&utm_medium=seo_notes&utm_source=recommendation";
    public static final String URL_POST = "https://www.zhihu.com/question/\\d+";

    private Site site = Site
            .me()
            .setDomain("www.zhihu01.com")
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
       // page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
        page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());
        //ÎÄÕÂÒ³
         if(selectUrl.regex(URL_POST).match()) {
             page.putField("", page.getHtml().xpath("//div[@id='root']//div[@class='List']/tidyText()"));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        System.setProperty("selenuim_config", "D:\\develop\\code\\Demo\\src\\main\\resources\\selenium_test.properties");
        //System.setProperty("selenuim_config", "/shuju/jar/selenium.properties");
        //System.setProperty("selenuim_config", "/shuju/jar/selenium.properties");

       // Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(
                SimpleProxyProvider.from(
                        new Proxy("http-dyn.abuyun.com", 9020, "H1ZT5X3356244N7D", "CCC97CDB4B85AD7E")
                ));

        Spider spider = Spider.create(new ZhiHuTest())
                .addUrl("https://www.zhihu.com/question/315422907")
                .addPipeline(new FilePipeline("D:\\jianshu"))
                //.addPipeline(new FilePipeline("/data/dynamic_spider/zhihu"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(
                        new BloomFilterDuplicateRemover(10000)))
                .setScheduler(new FileCacheQueueScheduler("D:\\jianshu\\urlfile"))
                //.setScheduler(new FileCacheQueueScheduler("/data/dynamic_spider/zhihu/urlfile"))
                 .setDownloader(httpClientDownloader)
                //.setDownloader(new SeleniumDownloader("E:\\chromedriver_win32\\chromedriver.exe").setSleepTime(1000))
                .setDownloader(new SeleniumDownloader())
                .thread(5);

        Scheduler scheduler = spider.getScheduler();
        spider.run();
    }
}