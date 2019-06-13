package com.lenovo.test;

import com.lenovo.common.FileCacheQueueScheduler;
import com.lenovo.common.ProxyProvider;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Collections;
import java.util.List;

/**
 *爱奇艺电影资料抓取
 */
public class Movie implements PageProcessor {

    public static final String URL_LIST = "https://list.iqiyi.com/www/1/-------------24-\\d+\\-1-iqiyi--.html";
    public static final String URL_POST = "https://www.iqiyi.com/v_\\w+\\.html";

    private Site site = Site
            .me()
            .setDomain("www.iqiyi.movie.com")
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

        Selectable selectable = page.getHtml().xpath("//*[@id=\"mod-singerlist\"]/ul/li[1]/a");
        System.out.println();

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        System.setProperty("selenuim_config", "D:\\develop\\code\\Demo\\src\\main\\resources\\selenium_test.properties");

        //动态代理方法调用
        ProxyProvider httpClientDownloader = new ProxyProvider();
        HttpClientDownloader downloaderAndProxy = httpClientDownloader.createDownloaderAndProxy();

        Spider spider = Spider.create(new Movie())
                .addUrl("https://y.qq.com/portal/singer_list.html")
                .addPipeline(new FilePipeline("D:\\data"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(
                        new BloomFilterDuplicateRemover(10000)))
                .setScheduler(new FileCacheQueueScheduler("D:\\data\\urlfile"))
                 .setDownloader(downloaderAndProxy)
                .setDownloader(new SeleniumDownloader())
                .thread(1);

        Scheduler scheduler = spider.getScheduler();
        spider.run();
    }
}