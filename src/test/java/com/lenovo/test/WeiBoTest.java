package com.lenovo.test;

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
 *Œ¢≤©≤‚ ‘
 */
public class WeiBoTest implements PageProcessor {

   public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";
   public static final String URL_LIST1 = "http://blog.sina.com.cn/lm/";
   public static final String URL_LIST2 = "http://blog.sina.com.cn/u/\\d+";
   public static final String URL_LIST3 = "http://blog\\.sina\\.com\\.cn/s/articlelist_\\w+_0_\\d+\\.html";
   public static final String URL_LIST4 = "http://blog.sina.com.cn/lm/\\w+";

    private Site site = Site
            .me()
            .setDomain("blog.sina.com.cn_Test")
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
        page.addTargetRequests(page.getHtml().links().regex(URL_LIST2).all());
        //≤©øÕ¡¥Ω”
//        if(selectUrl.regex(URL_LIST2).match()){
//            page.addTargetRequests(page.getHtml().links().regex(URL_LIST2).all());
//        }
        //Œƒ’¬“≥
        if(selectUrl.regex(URL_POST).match()){
            page.putField("", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']/tidyText()"));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        httpClientDownloader.setProxyProvider(
                SimpleProxyProvider.from(
                        new Proxy("http-dyn.abuyun.com", 9020, "H1ZT5X3356244N7D", "CCC97CDB4B85AD7E")
                ));

        Spider spider = Spider.create(new WeiBoTest())
                //.addUrl("http://blog.sina.com.cn/fhx686")
                .addUrl("http://blog.sina.com.cn/s/blog_179e2bc6b0102yw4a.html")
                .addPipeline(new FilePipeline("D:\\jianshu"))
                //.addPipeline(new FilePipeline("/home/shuju/weibo"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(
                        new BloomFilterDuplicateRemover(10000)))
                //.setScheduler(new FileCacheQueueScheduler("/home/shuju/weibo/urlfile"))
                .setScheduler(new FileCacheQueueScheduler("D:\\jianshu\\urlfile"))
                .setDownloader(httpClientDownloader)
                .thread(20);
        Scheduler scheduler = spider.getScheduler();
        spider.run();
    }
}