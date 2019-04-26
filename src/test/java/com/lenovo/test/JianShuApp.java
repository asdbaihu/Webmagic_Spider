package com.lenovo.test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Selectable;

/**
 *简书测试
 */
public class JianShuApp implements PageProcessor {

    //public static final String URL_LIST = "https://www.jianshu.com/u/\\w+\\?utm_campaign=maleskine&utm_content=user&utm_medium=seo_notes&utm_source=recommendation";
    public static final String URL_POST = "https://s0.jianshuapi.com/v3/notes/\\d+\\?read_mode=day&font_size=normal";

    private Site site = Site
            .me()
            .setDomain("jianshuTest.com")
            .setUseGzip(true)
            .addHeader("Content-Type", "application/json")
            .addHeader("Connection", "Keep-Alive")
            .setSleepTime(3000)
            .setUserAgent(
                    "Dalvik/2.1.0 (Linux; U; Android 6.0; Redmi Note 4 MIUI/V10.2.1.0.MBFCNXM) okhttp/3.3.0 haruki/4.9.1");

    @Override
    public void process(Page page) {
        Selectable selectUrl = page.getUrl();
        //page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
        //博客链接
//        if(selectUrl.regex(URL_LIST).match()){
        page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());
        //}
        //文章页
         if(selectUrl.regex(URL_POST).match()) {
            page.putField("", page.getHtml().xpath("//div[@class='article']//div[@class='show-content']/tidyText()"));
        }

//        else if (page.getHtml().links().regex(URL_LIST).smartContent() == null){
//            //设置skip之后，这个页面的结果不会被Pipeline处理
//            page.setSkip(true);
//        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        Spider spider = Spider.create(new JianShuApp())
                .addUrl("https://s0.jianshuapi.com/v3/notes/45577694?read_mode=day&font_size=normal")
                .addPipeline(new FilePipeline("D:\\jianshu"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                //.setScheduler(new RedisScheduler("127.0.0.1",6379))
                .setScheduler(new FileCacheQueueScheduler("D:\\jianshu\\urlfile"))
                .thread(5);
        Scheduler scheduler = spider.getScheduler();
        spider.run();
    }
}