package com.lenovo.test;

import com.google.common.hash.BloomFilter;
import com.lenovo.common.SpikeFileCacheQueueScheduler;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.*;
import us.codecraft.webmagic.selector.Selectable;

/**
 *�������
 */
public class JianShuTest implements PageProcessor {

    public static final String URL_LIST = "https://www.jianshu.com/u/\\w+\\?utm_campaign=maleskine&utm_content=user&utm_medium=seo_notes&utm_source=recommendation";
    public static final String URL_POST = "https://www.jianshu.com/p/\\w+";

    private Site site = Site
            .me()
            .setDomain("jianshuTest.com")
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

        SpikeFileCacheQueueScheduler file=new SpikeFileCacheQueueScheduler("D:\\jianshu\\urlfile");
        file.setRegx(URL_LIST);
        file.setRegx(URL_POST);

        Spider spider = Spider.create(new JianShuTest())
                .addUrl("https://y.qq.com/portal/singer_list.html")
                .addPipeline(new FilePipeline("D:\\jianshu"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(10000)))
                //.setScheduler(new RedisScheduler("127.0.0.1",6379))
                .setScheduler(file)
                .thread(20);
        Scheduler scheduler = spider.getScheduler();
        spider.run();
    }
}