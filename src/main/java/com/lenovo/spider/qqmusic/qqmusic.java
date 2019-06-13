package com.lenovo.spider.qqmusic;

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
 *QQ音乐资料抓取
 */
public class qqmusic implements PageProcessor {

    public static final String URL_LIST = "https://y.qq.com/portal/singer_list.html#page=\\+d\\&";
    public static final String URL_POST = "https://y.qq.com/n/yqq/singer/\\+w\\.html#stat=y_new.singerlist.singername";

    private Site site = Site
            .me()
            .setDomain("y.qq.com")
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

        //列表页
        if(selectUrl.regex(URL_LIST).match()){
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            List<String> itemlist = page.getHtml().links().regex(URL_POST).all();
            if(!itemlist.isEmpty()) {
                for (String item : itemlist) {
                    page.addTargetRequests(Collections.singletonList(item));
                }
            }
            page.putField("mid", page.getHtml().xpath("//*[@id=\"mod-singerlist\"]/ul/li[1]/a"));
        }
        //文章页
        else if(selectUrl.regex(URL_POST).match()) {


            page.putField("年代", page.getHtml().css("body > div.main > div.mod_data > div > div.data__name > h1.data__name_txt.js_index","text"));


        }
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

        Spider spider = Spider.create(new qqmusic())
                .addUrl("https://y.qq.com/portal/singer_list.html#page=1&")
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