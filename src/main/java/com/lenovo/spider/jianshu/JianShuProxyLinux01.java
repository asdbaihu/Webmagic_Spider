package com.lenovo.spider.jianshu;

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

/**
 *ºÚ È01
 */
public class JianShuProxyLinux01 implements PageProcessor {

    public static final String URL_POST = "https://www.jianshu.com/p/\\w+\\?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation";

    private Site site = Site
            .me()
            .setDomain("jianshu01.com")
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
            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());
      if(selectUrl.regex(URL_POST).match()) {
            page.putField("", page.getHtml().xpath("//div[@class='article']//div[@class='show-content']/tidyText()"));
        }
    }
    @Override
    public Site getSite() {
        return site;
    }

}