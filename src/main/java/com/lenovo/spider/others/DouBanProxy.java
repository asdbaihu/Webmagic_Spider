package com.lenovo.spider.others;

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

/**∂π∞Íª∞Ã‚
 *https://www.douban.com/gallery/
 */
public class DouBanProxy implements PageProcessor {


    public static final String URL_LIST = "https://www.douban.com/doulist/\\d+\\";
    public static final String URL_POST = "https://book.douban.com/subject/\\d+\\";

    private Site site = Site
            .me()
            .setDomain("www.douban.com")
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
        page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
        if (selectUrl.regex(URL_POST).match()) {
                page.putField("", page.getHtml().xpath("//div[@id='content']//div[@class='related_info']/tidyText()"));
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

        Spider spider = Spider.create(new DouBanProxy())
                .addUrl("https://www.douban.com/doulist/111359648/")
                .addPipeline(new FilePipeline("D:\\shuju\\douban"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(
                        new BloomFilterDuplicateRemover(10000)))
                .setScheduler(new FileCacheQueueScheduler("D:\\shuju\\douban\\urlfile"))
                .setDownloader(httpClientDownloader)
                .thread(20);
        Scheduler scheduler = spider.getScheduler();
        spider.run();
    }
}